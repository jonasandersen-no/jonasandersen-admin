package no.jonasandersen.admin.application;

import com.jcraft.jsch.JSchException;
import com.panfutov.result.Result;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.ssh.CommandExecutor;
import no.jonasandersen.admin.adapter.out.ssh.ExecutedCommand;
import no.jonasandersen.admin.adapter.out.ssh.FileExecutor;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.domain.Username;
import no.jonasandersen.admin.domain.VolumeId;
import no.jonasandersen.admin.infrastructure.AdminProperties.Linode;
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
  private final OutputListener<SensitiveString> passwordOutputListener = new OutputListener<>();
  private final OutputListener<LinodeInstance> instanceOutputListener = new OutputListener<>();
  private final Linode linodeProperties;
  private final LinodeVolumeService linodeVolumeService;

  public static ServerGenerator create(
      LinodeService service,
      SensitiveString defaultPassword,
      FileExecutor executor,
      DnsService dnsService,
      DeleteLinodeInstance deleteLinodeInstance,
      Linode linodeProperties,
      LinodeVolumeService linodeVolumeService)
      throws JSchException {
    return new ServerGenerator(
        service,
        defaultPassword,
        executor,
        dnsService,
        deleteLinodeInstance,
        CommandExecutor.create(),
        linodeProperties,
        linodeVolumeService);
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(
        LinodeService.createNull(),
        SensitiveString.of("Password123!"),
        FileExecutor.createNull(),
        DnsService.configureForTest(),
        DeleteLinodeInstance.configureForTest(),
        CommandExecutor.createNull(),
        new Linode("base", "token", "password", 1L),
        LinodeVolumeService.createNull());
  }

  public static ServerGenerator configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());
    return new ServerGenerator(
        LinodeService.createNull(),
        SensitiveString.of("Password123!"),
        FileExecutor.createNull(),
        DnsService.configureForTest(),
        DeleteLinodeInstance.configureForTest(),
        config.commandExecutor,
        new Linode("base", "token", "password", 1L),
        LinodeVolumeService.createNull());
  }

  public static ServerGenerator configureForTest() {
    return new ServerGenerator(
        LinodeService.createNull(),
        SensitiveString.of("Password123!"),
        FileExecutor.createNull(),
        DnsService.configureForTest(),
        DeleteLinodeInstance.configureForTest(),
        CommandExecutor.createNull(),
        new Linode("base", "token", "password", 1L),
        LinodeVolumeService.createNull());
  }

  private ServerGenerator(
      LinodeService service,
      SensitiveString defaultPassword,
      FileExecutor fileExecutor,
      DnsService dnsService,
      DeleteLinodeInstance deleteLinodeInstance,
      CommandExecutor commandExecutor,
      Linode linodeProperties,
      LinodeVolumeService linodeVolumeService) {
    this.service = service;
    this.defaultPassword = defaultPassword;
    this.fileExecutor = fileExecutor;
    this.dnsService = dnsService;
    this.deleteLinodeInstance = deleteLinodeInstance;
    this.commandExecutor = commandExecutor;
    this.linodeProperties = linodeProperties;
    this.linodeVolumeService = linodeVolumeService;
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

  public ServerGeneratorResponse generate(
      String owner, ServerType serverType, Subdomain subdomain) {
    return generate(owner, defaultPassword, serverType, subdomain);
  }

  public ServerGeneratorResponse generate(
      String owner, SensitiveString password, ServerType serverType) {
    return generate(owner, password, serverType, Subdomain.generate());
  }

  public ServerGeneratorResponse generate(
      String owner, SensitiveString password, ServerType serverType, Subdomain subdomain) {
    switch (serverType) {
      case MINECRAFT -> {
        passwordOutputListener.track(password);
        LinodeInstance instance =
            service.createDefaultMinecraftInstance(owner, password, subdomain);
        log.info("Created instance: {}", instance);
        instanceOutputListener.track(instance);

        if (!Features.isEnabled(Feature.LINODE_STUB) && subdomain != null) {

          Result<Void> result = null;
          try {
            result =
                dnsService.createOrReplaceRecord(
                    new Ip(instance.ip().getFirst()), owner, subdomain);

            // This isnt working? Drop results?
            if (result.isFailure()) {
              log.warn("Failed to create DNS record: {}", result.getErrors());
              log.warn("Deleting instance: {}", instance);
              //              deleteLinodeInstance.delete(instance.linodeId());
            }
          } catch (Exception e) {
            log.warn("Failed to create DNS record", e);
            log.warn("Deleting instance: {}", instance);
            //            deleteLinodeInstance.delete(instance.linodeId());
          }
        }

        Thread.ofVirtual()
            .start(
                () -> {

                  // Wait until booted and running. Check if ssh port can be connected to?
                  long maxWaitTime = 10 * 60 * 1000; // 10 minutes in milliseconds
                  try {
                    waitForPort(instance.ip().getFirst(), 22, 1000, 5000, maxWaitTime);
                  } catch (InterruptedException | IOException e) {
                    log.error("Something went wrong while waiting for server to get boot", e);
                  }
                  //          attachVolume(instance, volumeId);
                  attachVolume(instance, linodeProperties.volumeId());

                  PasswordConnectionInfo connectionInfo =
                      new PasswordConnectionInfo(
                          "root",
                          SensitiveString.of(password.value()),
                          new Ip(instance.ip().getFirst()),
                          22);

                  try {
                    String baseVolumeName = "scsi-0Linode_Volume_";
                    String volumeName = "minecraft-volume-02"; // Get from volume info
                    String fullVolumeName = baseVolumeName + volumeName;
                    String baseMountDir = "/mnt/";
                    String mountDir = baseMountDir + volumeName;
                    waitForVolumeDevice(connectionInfo, fullVolumeName, maxWaitTime, 1000);
                    ensureFilesystemExists(connectionInfo, fullVolumeName, maxWaitTime, 1000);
                    mountVolume(connectionInfo, fullVolumeName, mountDir);
                    log.info("All good, ready to install minecraft");
                    downloadAndRunDockerCompose(connectionInfo, mountDir);
                  } catch (IOException | InterruptedException | JSchException e) {
                    log.error("Something went wrong while waiting for volume device", e);
                  }
                });

        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

  public static boolean downloadAndRunDockerCompose(ConnectionInfo connectionInfo, String mountDir)
      throws IOException, JSchException, InterruptedException {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      return true;
    }

    CommandExecutor commandExecutor = CommandExecutor.create(connectionInfo);
    try {
      commandExecutor.connect();

      // Download docker-compose.yml
      ExecutedCommand downloadResult =
          commandExecutor.executeCommand(
              "curl -fsSL https://raw.githubusercontent.com/NotBjoggisAtAll/minecraft-compose/main/testing/docker-compose.yml -o docker-compose.yml");
      if (downloadResult.statusCode() != 0) {
        log.error(
            "Error downloading docker-compose.yml. Status code: {}, Output: {}",
            downloadResult.statusCode(),
            downloadResult.output());
        return false;
      }

      // Run docker compose up -d
      String volumeDir = mountDir + "/minecraft-data"; // Or get it from elsewhere
      ExecutedCommand composeUpResult =
          commandExecutor.executeCommand(
              "MINECRAFT_VOLUME_DIR=" + volumeDir + " docker compose up -d");
      if (composeUpResult.statusCode() != 0) {
        log.error(
            "Error running docker compose up. Status code: {}, Output: {}",
            composeUpResult.statusCode(),
            composeUpResult.output());
        return false;
      }

      // Show running containers
      ExecutedCommand dockerPsResult = commandExecutor.executeCommand("docker ps -a");
      if (dockerPsResult.statusCode() != 0) {
        log.error(
            "Error running docker ps. Status code: {}, Output: {}",
            dockerPsResult.statusCode(),
            dockerPsResult.output());
        return false;
      }
      log.info(dockerPsResult.output());

      return true;
    } finally {
      try {
        commandExecutor.disconnect();
      } catch (Exception e) {
        // Handle errors
      }
    }
  }

  public static boolean mountVolume(
      ConnectionInfo connectionInfo, String volumeName, String mountPath)
      throws IOException, JSchException {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      return true;
    }

    CommandExecutor commandExecutor = CommandExecutor.create(connectionInfo);
    try {
      commandExecutor.connect();
      commandExecutor.executeCommand("mkdir \"" + mountPath + "\"");
      ExecutedCommand result =
          commandExecutor.executeCommand(
              "mount /dev/disk/by-id/" + volumeName + " \"" + mountPath + "\"");
      commandExecutor.disconnect();
      if (result.statusCode() != 0) {
        System.out.println(
            "Error mounting volume. Status code: "
                + result.statusCode()
                + ", Output: "
                + result.output());
      }
      return result.statusCode() == 0;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        commandExecutor.disconnect();
      } catch (Exception e) {
        // Handle errors
      }
    }
  }

  public static void ensureFilesystemExists(
      ConnectionInfo connectionInfo, String volumeName, long maxWaitTime, int retryInterval)
      throws IOException, InterruptedException, JSchException {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      return;
    }
    long startTime = System.currentTimeMillis();
    boolean filesystemExists = false;

    while (!filesystemExists) {
      long elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime >= maxWaitTime) {
        throw new IOException(
            "Filesystem on "
                + volumeName
                + " did not become ready within the specified timeout ("
                + maxWaitTime
                + "ms).");
      }

      CommandExecutor commandExecutor = CommandExecutor.create(connectionInfo);
      try {
        commandExecutor.connect();
        ExecutedCommand result =
            commandExecutor.executeCommand(
                "blkid /dev/disk/by-id/" + volumeName + " | grep -q 'TYPE=\"ext4\"'");
        if (result.statusCode() == 0) {
          log.info("Filesystem already exists on {}", volumeName);
          filesystemExists = true;
        } else {
          log.info("Creating filesystem on {}", volumeName);
          result = commandExecutor.executeCommand("mkfs.ext4 /dev/disk/by-id/" + volumeName);
          if (result.statusCode() == 0) {
            log.info("Filesystem created successfully on {}", volumeName);
            filesystemExists = true;
          } else {
            log.info(
                "Error creating filesystem on {}. Status code: {}, Output: {}",
                volumeName,
                result.statusCode(),
                result.output());
            Thread.sleep(retryInterval);
          }
        }
      } finally {
        try {
          commandExecutor.disconnect();
        } catch (Exception e) {
          // Handle errors
        }
      }
    }
  }

  private void attachVolume(LinodeInstance instance, Long volumeId) {
    linodeVolumeService.attachVolume(instance.linodeId(), VolumeId.from(volumeId));
  }

  public static void waitForPort(
      String ip, int port, int timeout, int retryInterval, long maxWaitTime)
      throws InterruptedException, IOException {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      return;
    }

    long startTime = System.currentTimeMillis();
    boolean connected = false;

    while (!connected) {
      long elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime >= maxWaitTime) {
        throw new IOException(
            "Port "
                + port
                + " did not become available within the specified timeout ("
                + maxWaitTime
                + "ms).");
      }

      try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(ip, port), timeout);
        connected = true;
        log.info("Port {} is now open on {}", port, ip);
      } catch (IOException e) {
        log.info(
            "Port {} is not yet open. Retrying in {}ms. Elapsed time: {}ms",
            port,
            retryInterval,
            elapsedTime);
        Thread.sleep(retryInterval);
      }
    }
  }

  public static void waitForVolumeDevice(
      ConnectionInfo connectionInfo, String volumeDevice, long maxWaitTime, int retryInterval)
      throws IOException, InterruptedException, JSchException {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      return;
    }

    long startTime = System.currentTimeMillis();
    boolean deviceFound = false;

    while (!deviceFound) {
      long elapsedTime = System.currentTimeMillis() - startTime;
      if (elapsedTime >= maxWaitTime) {
        throw new IOException(
            "Volume device "
                + volumeDevice
                + " did not become available within the specified timeout ("
                + maxWaitTime
                + "ms).");
      }

      CommandExecutor commandExecutor = CommandExecutor.create(connectionInfo);
      try {
        commandExecutor.connect();
        ExecutedCommand result =
            commandExecutor.executeCommand("ls -al /dev/disk/by-id/ | grep -q " + volumeDevice);
        commandExecutor.disconnect();

        if (result.statusCode() == 0) {
          log.info("Volume device {} found.", volumeDevice);
          deviceFound = true;
        } else {
          log.info(
              "Volume device {} not yet found. Retrying in {}ms. Elapsed time: {}ms",
              volumeDevice,
              retryInterval,
              elapsedTime);
          Thread.sleep(retryInterval);
        }

      } finally {
        try {
          commandExecutor.disconnect();
        } catch (Exception e) {
          // Handle errors
        }
      }
    }
  }

  public static class Config {

    private CommandExecutor commandExecutor;

    public Config setCommandExecutor(CommandExecutor commandExecutor) {
      this.commandExecutor = commandExecutor;
      return this;
    }
  }
}
