package no.jonasandersen.admin.core.minecraft.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MinecraftInstanceTest {

  @Test
  void ipAddressIsValidFormat() {
    assertThatCode(() -> new MinecraftInstance("Test", "127.0.0.1"))
        .doesNotThrowAnyException();
  }

  @Test
  void ipAddressIsInvalidFormat() {
    assertThatThrownBy(() -> new MinecraftInstance("Test", "127.0.0.1.1"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isEmptyReturnsTrueWhenNoFieldsAreSet() {
    MinecraftInstance instance = new MinecraftInstance();
    assertThat(instance.isEmpty()).isTrue();
  }

  @Test
  void isEmptyReturnsFalseWhenNameIsSet() {
    MinecraftInstance instance = new MinecraftInstance();
    instance.setName("Test");
    assertThat(instance.isEmpty()).isFalse();
  }
}