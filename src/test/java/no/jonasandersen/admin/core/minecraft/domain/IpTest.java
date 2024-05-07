package no.jonasandersen.admin.core.minecraft.domain;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class IpTest {

  @Test
  void ipAddressIsValidFormat() {
    assertThatCode(() -> new Ip("127.0.0.1")).doesNotThrowAnyException();
  }

  @Test
  void ipAddressIsInvalidFormat() {
    assertThatThrownBy(() -> new Ip("1234.1234.1234.1234")).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void throwsExceptionWhenValueIsNull() {
    assertThatThrownBy(() -> new Ip(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Value cannot be null");
  }
}