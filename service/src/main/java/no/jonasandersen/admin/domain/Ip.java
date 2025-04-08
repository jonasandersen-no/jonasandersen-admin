package no.jonasandersen.admin.domain;

import org.jetbrains.annotations.NotNull;

public record Ip(String value) {

  public static final String REGEX =
      "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";

  public Ip {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }

    if (!value.matches(REGEX)) {
      throw new IllegalArgumentException("Invalid IP address");
    }
  }

  public static @NotNull Ip localhostIp() {
    return new Ip("127.0.0.1");
  }
}
