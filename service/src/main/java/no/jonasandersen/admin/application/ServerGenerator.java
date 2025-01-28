package no.jonasandersen.admin.application;

import com.jcraft.jsch.JSchException;
import com.panfutov.result.Result;
import java.io.IOException;
import java.time.Duration;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.ssh.CommandExecutor;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.adapter.out.ssh.Retryer;
import no.jonasandersen.admin.domain.CommandExecutionFailedException;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.PrivateKeyConnectionInfo;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.domain.Username;
import no.jonasandersen.admin.infrastructure.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGenerator {

  private static final Logger log = LoggerFactory.getLogger(ServerGenerator.class);

  private final LinodeService service;
  private final SensitiveString defaultPassword;
  private final FileExecutor fileExecutor;
  private final DnsService dnsService;
  private final DeleteLinodeInstance deleteLinodeInstance;
  private final CommandExecutor commandExecutor;
  private final ControlCenterProperties controlCenterProperties;
  private final OutputListener<SensitiveString> passwordOutputListener = new OutputListener<>();
  private final OutputListener<LinodeInstance> instanceOutputListener = new OutputListener<>();


  public static ServerGenerator create(LinodeService service,
      SensitiveString defaultPassword,
      FileExecutor executor,
      ControlCenterProperties controlCenterProperties, DnsService dnsService,
      DeleteLinodeInstance deleteLinodeInstance)
      throws JSchException {
    return new ServerGenerator(service, defaultPassword, executor, dnsService,
        deleteLinodeInstance, CommandExecutor.create(), controlCenterProperties);
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"),
        FileExecutor.createNull(), DnsService.configureForTest(), DeleteLinodeInstance.configureForTest(),
        CommandExecutor.createNull(),
        ControlCenterProperties.configureForTest());
  }

  public static ServerGenerator configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"),
        FileExecutor.createNull(), DnsService.configureForTest(), DeleteLinodeInstance.configureForTest(),
        config.commandExecutor,
        ControlCenterProperties.configureForTest());

  }

  public static ServerGenerator configureForTest() {
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"),
        FileExecutor.createNull(), DnsService.configureForTest(), DeleteLinodeInstance.configureForTest(),
        CommandExecutor.createNull(),
        ControlCenterProperties.configureForTest());
  }

  private ServerGenerator(LinodeService service,
      SensitiveString defaultPassword,
      FileExecutor fileExecutor,
      DnsService dnsService,
      DeleteLinodeInstance deleteLinodeInstance,
      CommandExecutor commandExecutor,
      ControlCenterProperties controlCenterProperties) {
    this.service = service;
    this.defaultPassword = defaultPassword;
    this.fileExecutor = fileExecutor;
    this.dnsService = dnsService;
    this.deleteLinodeInstance = deleteLinodeInstance;
    this.commandExecutor = commandExecutor;
    this.controlCenterProperties = controlCenterProperties;
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

        if (!Features.isEnabled(Feature.LINODE_STUB) && subdomain != null) {

          Result<Void> result = null;
          try {
            result = dnsService.createOrReplaceRecord(new Ip(instance.ip().getFirst()), owner,
                subdomain);

            // This isnt working? Drop results?
            if (result.isFailure()) {
              log.warn("Failed to create DNS record: {}", result.getErrors());
              log.warn("Deleting instance: {}", instance);
              deleteLinodeInstance.delete(instance.linodeId());
            }
          } catch (Exception e) {
            log.warn("Failed to create DNS record", e);
            log.warn("Deleting instance: {}", instance);
            deleteLinodeInstance.delete(instance.linodeId());
          }

        }

        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

  public void install(LinodeId from, SensitiveString password, ServerType serverType) {
    LinodeInstance instance = service.findInstanceById(from)
        .orElseThrow(() -> new IllegalArgumentException("Instance not found"));

    PasswordConnectionInfo connectionInfo = new PasswordConnectionInfo("root", SensitiveString.of(password.value()),
        new Ip(instance.ip().getFirst()), 22);

    runFile(connectionInfo, "install-docker.txt");
    runFile(connectionInfo, "setup-minecraft.txt");

  }

  public String generateViaAnsible(String command) throws JSchException, IOException, InterruptedException {
    String privateKey = controlCenterProperties.privateKeyFilePath();
    String username = controlCenterProperties.username();
    String ip = controlCenterProperties.ip();
    Integer port = controlCenterProperties.port();

    PrivateKeyConnectionInfo controlCenter =
        new PrivateKeyConnectionInfo(username, SensitiveString.of(privateKey), new Ip(ip), port);

    CommandExecutor commandExecutor = CommandExecutor.create(controlCenter);
    commandExecutor.connect();
    String result = commandExecutor.executeCommand(command);
    commandExecutor.disconnect();
    return result;
  }

  private void runFile(PasswordConnectionInfo connectionInfo, String file) {
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

  public static class Config {

    private CommandExecutor commandExecutor;

    public Config setCommandExecutor(CommandExecutor commandExecutor) {
      this.commandExecutor = commandExecutor;
      return this;
    }
  }
}
