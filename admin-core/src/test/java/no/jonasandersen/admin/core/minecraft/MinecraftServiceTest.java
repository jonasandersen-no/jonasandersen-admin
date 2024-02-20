package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MinecraftServiceTest {

  private final MinecraftService service = new MinecraftService();

  @Nested
  class Find {

    @Test
    void namePrefixIsCorrect() {
      assertThat(service.findMinecraftInstance().getName()).startsWith("minecraft-auto-config-");
    }

    @Test
    void ipIsFormattedCorrectly() {
      assertThat(service.findMinecraftInstance().getIp()).matches(
          "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }
  }
}
