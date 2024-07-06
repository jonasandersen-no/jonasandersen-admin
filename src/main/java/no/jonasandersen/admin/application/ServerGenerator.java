package no.jonasandersen.admin.application;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.time.Duration;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.adapter.out.ssh.Retryer;
import no.jonasandersen.admin.domain.CommandExecutionFailedException;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGenerator {

  private static final Logger log = LoggerFactory.getLogger(ServerGenerator.class);

  private final LinodeService service;
  private final SensitiveString defaultPassword;
  private final FileExecutor fileExecutor;
  private final OutputListener<SensitiveString> passwordOutputListener = new OutputListener<>();
  private final OutputListener<LinodeInstance> instanceOutputListener = new OutputListener<>();

  public static ServerGenerator create(LinodeService service, SensitiveString defaultPassword, FileExecutor executor) {
    return new ServerGenerator(service, defaultPassword, executor);
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"),
        FileExecutor.createNull());
  }

  private ServerGenerator(LinodeService service, SensitiveString defaultPassword, FileExecutor fileExecutor) {
    this.service = service;
    this.defaultPassword = defaultPassword;
    this.fileExecutor = fileExecutor;
  }

  public OutputTracker<SensitiveString> passwordTracker() {
    return passwordOutputListener.createTracker();
  }

  public OutputTracker<LinodeInstance> instanceTracker() {
    return instanceOutputListener.createTracker();
  }

  public ServerGeneratorResponse generate(Username owner, ServerType serverType) {
    return generate(owner.value(), serverType);
  }

  public ServerGeneratorResponse generate(String owner, ServerType serverType) {
    return generate(owner, defaultPassword, serverType, Subdomain.generate());
  }

  public ServerGeneratorResponse generate(String owner, ServerType serverType, Subdomain subdomain) {
    return generate(owner, defaultPassword, serverType, subdomain);
  }

  public ServerGeneratorResponse generate(String owner, SensitiveString password, ServerType serverType) {
    return generate(owner, password, serverType, Subdomain.generate());
  }

  public ServerGeneratorResponse generate(String owner, SensitiveString password, ServerType serverType,
      Subdomain subdomain) {
    switch (serverType) {
      case MINECRAFT -> {
        passwordOutputListener.track(password);
        LinodeInstance instance = service.createDefaultMinecraftInstance(owner, password, subdomain);
        log.info("Created instance: {}", instance);
        instanceOutputListener.track(instance);
        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

  public void install(LinodeId from, SensitiveString password, ServerType serverType) {
    LinodeInstance instance = service.findInstanceById(from)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found"));

    ConnectionInfo connectionInfo = new ConnectionInfo("root", SensitiveString.of(password.value()),
        new Ip(instance.ip().getFirst()), 22);

    runFile(connectionInfo, "install-docker.txt");
    runFile(connectionInfo, "setup-minecraft.txt");

  }

  private void runFile(ConnectionInfo connectionInfo, String file) {
    Retryer.retry(() -> {
      try {
        log.info("Executing file: {}", file);
        fileExecutor.setup(connectionInfo);
        fileExecutor.parseFile(file);
        log.info("Done executing file");
      } catch (JSchException | IOException | InterruptedException e) {
        log.warn("Failed to connect to instance: {}", e.getMessage());
        throw new CommandExecutionFailedException(e);
      } finally {
        fileExecutor.cleanup();
      }
    }, 20, Duration.ofMinutes(1));
  }
}
