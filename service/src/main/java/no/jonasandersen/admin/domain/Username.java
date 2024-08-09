package no.jonasandersen.admin.domain;

import java.util.Objects;

public record Username(String value) {

  public static Username create(String value) {
    Objects.requireNonNull(value);
    return new Username(value);
  }

  public static Username of(String value) {
    Objects.requireNonNull(value);
    return new Username(value);
  }

}
