package no.jonasandersen.admin.application;

import com.jcraft.jsch.JSchException;
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
  private final SetupConnection setupConnection;
  private final OutputListener<SensitiveString> passwordOutputListener = new OutputListener<>();
  private final OutputListener<LinodeInstance> instanceOutputListener = new OutputListener<>();

  public static ServerGenerator create(LinodeService service, SensitiveString defaultPassword) {
    return new ServerGenerator(service, defaultPassword, new RealSetupConnection());
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"),
        new StubSetupConnection());
  }

  private ServerGenerator(LinodeService service, SensitiveString defaultPassword,
      SetupConnection setupConnection) {
    this.service = service;
    this.defaultPassword = defaultPassword;
    this.setupConnection = setupConnection;
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
          FileExecutor fileExecutor = setupConnection.setupConnection(connectionInfo);
        } catch (JSchException e) {
          log.warn("Failed to connect to instance: {}", e.getMessage());
        }
        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

  private interface SetupConnection {

    FileExecutor setupConnection(ConnectionInfo connectionInfo) throws JSchException;
  }

  private final static class RealSetupConnection implements SetupConnection {

    @Override
    public FileExecutor setupConnection(ConnectionInfo connectionInfo) throws JSchException {
      CommandExecutor commandExecutor = CommandExecutor.create(connectionInfo);
      return FileExecutor.create(commandExecutor);
    }
  }

  private final static class StubSetupConnection implements SetupConnection {

    @Override
    public FileExecutor setupConnection(ConnectionInfo connectionInfo) throws JSchException {
      return FileExecutor.createNull();
    }
  }
}
