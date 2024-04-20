package no.jonasandersen.admin.adapter.out.ssh;


import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import no.jonasandersen.admin.AdminProperties.Minecraft;
import no.jonasandersen.admin.adapter.out.linode.Instance;
import no.jonasandersen.admin.adapter.out.linode.LinodeBjoggisExchange;
import no.jonasandersen.admin.adapter.out.linode.LinodeVolumeDto;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@Disabled
class SSHServiceTest extends IoBasedTest {

  public static final Long LINODE_ID = 56601153L;

  private static final Logger logger = LoggerFactory.getLogger(SSHServiceTest.class);

  @Autowired
  SSHService service;

  static class TestLinodeBjoggis implements LinodeBjoggisExchange {

    @Override
    public List<Instance> listInstances() {
      return List.of();
    }

    @Override
    public List<LinodeVolumeDto> getVolumes() {
      return List.of();
    }

    @Override
    public void attachVolume(Long volumeId, Long linodeId) {
      logger.info("FAKE: Attaching volume {} to linode {}", volumeId, linodeId);
    }

    @Override
    public void detachVolume(Long volumeId) {
      logger.info("FAKE: Detaching volume {}", volumeId);
    }
  }

  @Test
  void test() throws JSchException, IOException, InterruptedException {
    service.attachVolume(LINODE_ID);

    CompletableFuture<Void> future = service.runShellCommands(
        "classpath:install-docker.txt", "classpath:setup-minecraft.txt");
    future.join();
    logger.info("Test done");
  }

  @Test
  void detach() throws InterruptedException {
    service.detachVolume();
    logger.info("Detach done");
  }

  @Test
  void setupMinecraft() throws JSchException, IOException {
    CompletableFuture<Void> future = service.runShellCommands(
        "classpath:setup-minecraft.txt");
    future.join();
    logger.info("Docker install done");
  }

  @Test
  void uninstall() throws JSchException, IOException {
    CompletableFuture<Void> future = service.runShellCommands(
        "classpath:uninstall-minecraft.txt");
    future.join();
    logger.info("Uninstall done");

  }

  @TestConfiguration
  static class Config {

    @Bean
    SSHService sshService(LinodeBjoggisExchange linodeBjoggisExchange) throws JSchException {
      SSHService service = new SSHService(linodeBjoggisExchange, new Ip("172.232.142.240"),
          new Minecraft("root", "Spillhuset123", 22));
      return service;
    }


  }
}