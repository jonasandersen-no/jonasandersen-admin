package no.jonasandersen.admin.application;

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
