package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MinecraftServiceTest {

  private final MinecraftService service = new MinecraftService(new TestServerApi());

  @Nested
  class Find {

    @Test
    void namePrefixIsCorrect() {
      assertThat(service.findMinecraftInstance().getName()).startsWith("minecraft-auto-config-");
    }

  }
}
