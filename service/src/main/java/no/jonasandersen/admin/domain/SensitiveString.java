package no.jonasandersen.admin.domain;

import org.jspecify.annotations.NonNull;

public record SensitiveString(String value) {

  public static SensitiveString of(String value) {
    return new SensitiveString(value);
  }

  public static SensitiveString empty() {
    return new SensitiveString("");
  }

  public boolean isPresent() {
    return value != null && !value.isEmpty();
  }

  @Override
  public @NonNull String toString() {
    return isPresent() ? "<present>" : "<not present>";
  }
}
