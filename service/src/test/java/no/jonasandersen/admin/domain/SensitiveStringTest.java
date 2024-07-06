package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SensitiveStringTest {

  @Test
  void toStringContainsPresentWhenValueIsPresent() {
    var sensitiveString = SensitiveString.of("password");

    assertThat(sensitiveString.toString()).contains("<present>");
  }

  @Test
  void toStringContainsNotPresentWhenValueIsEmptyString() {
    var sensitiveString = SensitiveString.of("");

    assertThat(sensitiveString.toString()).contains("<not present>");
  }

  @Test
  void toStringContainsNotPresentWhenValueIsNull() {
    var sensitiveString = SensitiveString.of(null);

    assertThat(sensitiveString.toString()).contains("<not present>");
  }
}