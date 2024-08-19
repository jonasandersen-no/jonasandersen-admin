package no.jonasandersen.admin.domain;

public enum Feature {

  LINODE_STUB(true);

  private final boolean defaultValue;

  Feature(boolean defaultValue) {
    this.defaultValue = defaultValue;
  }

  public boolean defaultValue() {
    return defaultValue;
  }
}
