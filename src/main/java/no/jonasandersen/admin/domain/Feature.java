package no.jonasandersen.admin.domain;

public enum Feature {

  LINODE_STUB(true),
  ALLOW_ALL_USERS(false);

  private final boolean defaultValue;

  Feature(boolean defaultValue) {
    this.defaultValue = defaultValue;
  }

  public boolean defaultValue() {
    return defaultValue;
  }
}
