package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MeasurementTest {

  @Test
  void microCelsiusShouldBeConvertedToCelsius() {
    var temperature = new Measurement("20000", "0");

    assertThat(temperature.celsius().value()).isEqualTo("20.00");
  }

  @Test
  void microCelsiusWithDecimalsShouldBeConvertedToCelsius() {
    var temperature = new Measurement("20100", "0");

    assertThat(temperature.celsius().value()).isEqualTo("20.10");
  }

  @Test
  void celsiusShouldHaveTwoDecimals() {
    var temperature = new Measurement("12345", "0");

    assertThat(temperature.celsius().value()).isEqualTo("12.34");
  }

  @Test
  void inputShouldAlwaysBe5Characters() {
    assertThatThrownBy(() -> new Measurement("1234", "0"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Celsius must be 5 characters");
  }

}