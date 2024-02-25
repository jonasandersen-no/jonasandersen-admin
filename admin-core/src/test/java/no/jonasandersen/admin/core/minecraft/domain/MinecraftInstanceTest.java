package no.jonasandersen.admin.core.minecraft.domain;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MinecraftInstanceTest {

  @Test
  void ipAddressIsValidFormat() {
    assertThatCode(() -> new MinecraftInstance("Test", "127.0.0.1"))
        .doesNotThrowAnyException();

    assertThatCode(() -> new MinecraftInstance().setIp("127.0.0.1"))
        .doesNotThrowAnyException();
  }

  @Test
  void ipAddressIsInvalidFormat() {
    assertThatThrownBy(() -> new MinecraftInstance("Test", "127.0.0.1.1"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new MinecraftInstance().setIp("127.0.0.1.1"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}