package no.jonasandersen.admin.domain;

public enum Feature {
  STUB(true);

  private final boolean defaultValue;

  Feature(boolean defaultValue) {
    this.defaultValue = defaultValue;
  }

  public boolean defaultValue() {
    return defaultValue;
  }
}
