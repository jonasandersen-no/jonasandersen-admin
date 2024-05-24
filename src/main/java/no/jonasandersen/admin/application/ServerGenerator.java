package no.jonasandersen.admin.application;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.ssh.CommandExecutor;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.core.domain.ConnectionInfo;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.domain.SensitiveString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGenerator {

  private static final Logger log = LoggerFactory.getLogger(ServerGenerator.class);

  public enum ServerType {
    MINECRAFT
  }

  private final LinodeService service;
  private final SensitiveString defaultPassword;
  private final FileExecutor fileExecutor;
  private final OutputListener<SensitiveString> passwordOutputListener = new OutputListener<>();
  private final OutputListener<LinodeInstance> instanceOutputListener = new OutputListener<>();

  public static ServerGenerator create(LinodeService service, SensitiveString defaultPassword) {
    return new ServerGenerator(service, defaultPassword, FileExecutor.create());
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

  public ServerGeneratorResponse generate(ServerType serverType) {
    return generate(serverType, defaultPassword);
  }

  public ServerGeneratorResponse generate(ServerType serverType, SensitiveString password) {
    switch (serverType) {
      case MINECRAFT -> {
        passwordOutputListener.track(password);
        LinodeInstance instance = service.createDefaultMinecraftInstance(password);
        instanceOutputListener.track(instance);
        try {
          ConnectionInfo connectionInfo = new ConnectionInfo("root", SensitiveString.of(password.value()),
              new Ip(instance.ip().getFirst()), 22);
          log.info("Connecting to {}", connectionInfo);
          fileExecutor.setup(connectionInfo);
          //parse files
          CommandExecutor commandExecutor = fileExecutor.getCommandExecutor();
          commandExecutor.connect();
          commandExecutor.executeCommand("ls -al");
          commandExecutor.disconnect();
          fileExecutor.cleanup();

        } catch (JSchException | IOException e) {
          log.warn("Failed to connect to instance: {}", e.getMessage());
        }
        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }
}
