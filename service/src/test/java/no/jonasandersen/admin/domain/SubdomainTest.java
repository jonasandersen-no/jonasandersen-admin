package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SubdomainTest {

  @Test
  void valueCanNotBeNull() {

    assertThatThrownBy(() -> new Subdomain(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can not be null");
  }

  @Test
  void valueCanNotBeBlank() {

    assertThatThrownBy(() -> new Subdomain(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can not be blank");
  }

  @Test
  void valueCanNotContainWhitespace() {

    assertThatThrownBy(() -> new Subdomain("a b"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can not contain whitespace");
  }

  @Test
  void valueCanOnlyContainLettersNumbersAndDashes() {

    assertThatThrownBy(() -> new Subdomain("a!b"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can only contain letters and numbers and dashes");
  }

  @Test
  void valueCanNotBeLongerThan63Characters() {

    assertThatThrownBy(() -> new Subdomain("a".repeat(64)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can not be longer than 63 characters");
  }

  @Test
  void valueCanNotBeShorterThan3Characters() {

    assertThatThrownBy(() -> new Subdomain("a"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value can not be shorter than 3 characters");
  }

  @Test
  void validValue() {
    assertThatNoException().isThrownBy(() -> new Subdomain("abc"));
    assertThatNoException().isThrownBy(() -> new Subdomain("a-b-c"));
  }
}