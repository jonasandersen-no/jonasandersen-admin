package no.jonasandersen.admin.domain;

import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;

public record Measurement(LocalDateTime timestamp, Celsius celsius, Humidity humidity) {

  public Measurement(Celsius celsius, Humidity humidity) {
    this(LocalDateTime.now(), celsius, humidity);
  }

  public Measurement(String celsius, String humidity) {
    this(new Celsius(convertCelsius(celsius)), new Humidity(Integer.parseInt(humidity)));
  }

  private static @NotNull String convertCelsius(String celsius) {
    if (celsius.length() != 5) {
      throw new IllegalArgumentException("Celsius must be 5 characters");
    }

    String first = celsius.substring(0, 2);
    String last = celsius.substring(2, celsius.length() - 1);

    return first + "." + last;
  }

  public record Celsius(String value) {}

  public record Humidity(int value) {}
}
