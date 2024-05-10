package no.jonasandersen.admin.domain;

public record SensitiveString(String value) {

  public static SensitiveString of(String value) {
    return new SensitiveString(value);
  }

  public boolean isPresent() {
    return value != null && !value.isEmpty();
  }

  @Override
  public String toString() {
    return isPresent() ? "<present>" : "<not present>";
  }
}
