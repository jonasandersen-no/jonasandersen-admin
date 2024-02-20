package no.jonasandersen.admin.core.minecraft;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MinecraftServiceTest {

  @Test
  void findMinecraftInstanceReturnsInstance() {
    MinecraftService service = new MinecraftService();

    assertThat(service.findMinecraftInstance()).isNotNull();
    assertThat(service.findMinecraftInstance().getName()).isEqualTo("minecraft");
  }
}
