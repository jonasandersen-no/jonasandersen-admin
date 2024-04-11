package no.jonasandersen.admin.adapter.out.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import no.jonasandersen.admin.AdminProperties.Minecraft;
import no.jonasandersen.admin.adapter.out.linode.LinodeBjoggisExchange;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ResourceUtils;

public class SSHService {

  public static final long VOLUME_ID = 2837884L;
  private final Logger logger = LoggerFactory.getLogger(SSHService.class);

  private final Session session;

  private final LinodeBjoggisExchange linodeBjoggisExchange;

  private int sleepAfterVolumeAttachSeconds = 20;
  private int sleepAfterVolumeDetachSeconds = 20;

  public SSHService(LinodeBjoggisExchange linodeBjoggisExchange,
      Ip ip, Minecraft minecraft)
      throws JSchException {
    this.linodeBjoggisExchange = linodeBjoggisExchange;

    JSch jsch = new JSch();
    session = jsch.getSession(minecraft.username(), ip.value(), minecraft.port());
    session.setPassword(minecraft.password());

    logger.info("Session created for ip {}", ip);

    // Avoid asking for key confirmation
    Properties config = new Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);
  }

  @Async
  public CompletableFuture<Void> runShellCommands(String... commandFiles)
      throws IOException, JSchException {
    session.connect();

    for (String commandFile : commandFiles) {
      BufferedReader bufferedReader = new BufferedReader(
          new FileReader(ResourceUtils.getFile(commandFile)));

      while (bufferedReader.ready()) {
        String command = bufferedReader.readLine();
        String result = new ConsoleCommand(command).execute(session);
        logger.info("Result: {}", result);
      }
      bufferedReader.close();
    }

    session.disconnect();
    return CompletableFuture.completedFuture(null);
  }

  public void attachVolume(Long linodeId) throws InterruptedException {
    linodeBjoggisExchange.attachVolume(VOLUME_ID, linodeId);

    logger.info(
        STR."Sleeping for \{sleepAfterVolumeAttachSeconds} seconds to wait for volume to attach");
    Thread.sleep(Duration.ofSeconds(sleepAfterVolumeAttachSeconds).toMillis());
  }

  public void detachVolume() throws InterruptedException {
    linodeBjoggisExchange.detachVolume(VOLUME_ID);

    logger.info(
        STR."Sleeping for \{sleepAfterVolumeDetachSeconds} seconds to wait for volume to detach");
    Thread.sleep(Duration.ofSeconds(sleepAfterVolumeDetachSeconds).toMillis());
  }

  public int getSleepAfterVolumeAttachSeconds() {
    return sleepAfterVolumeAttachSeconds;
  }

  public void setSleepAfterVolumeAttachSeconds(int sleepAfterVolumeAttachSeconds) {
    this.sleepAfterVolumeAttachSeconds = sleepAfterVolumeAttachSeconds;
  }

  public int getSleepAfterVolumeDetachSeconds() {
    return sleepAfterVolumeDetachSeconds;
  }

  public void setSleepAfterVolumeDetachSeconds(int sleepAfterVolumeDetachSeconds) {
    this.sleepAfterVolumeDetachSeconds = sleepAfterVolumeDetachSeconds;
  }
}