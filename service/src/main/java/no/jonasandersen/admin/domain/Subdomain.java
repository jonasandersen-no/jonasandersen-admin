package no.jonasandersen.admin.domain;

public record Subdomain(String value) {

  public static Subdomain of(String value) {
    return new Subdomain(value);
  }

  public static Subdomain generate() {
    return new Subdomain("auto-generated-" + System.currentTimeMillis());
  }

  public Subdomain {
    if (value == null) {
      throw new IllegalArgumentException("value can not be null");
    }

    if (value.isBlank()) {
      throw new IllegalArgumentException("value can not be blank");
    }

    if (value.contains(" ")) {
      throw new IllegalArgumentException("value can not contain whitespace");
    }

    if (!value.matches("^[a-zA-Z0-9-]*$")) {
      throw new IllegalArgumentException("value can only contain letters and numbers and dashes");
    }

    if (value.length() > 63) {
      throw new IllegalArgumentException("value can not be longer than 63 characters");
    }

    if (value.length() < 3) {
      throw new IllegalArgumentException("value can not be shorter than 3 characters");
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
