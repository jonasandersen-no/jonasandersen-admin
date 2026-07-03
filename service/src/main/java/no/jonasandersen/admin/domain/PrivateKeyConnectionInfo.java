package no.jonasandersen.admin.domain;

public final class PrivateKeyConnectionInfo implements ConnectionInfo {

  private final String username;
  private final SensitiveString privateKey;
  private final Connection address;
  private final int port;

  public PrivateKeyConnectionInfo(
      String username, SensitiveString privateKey, Connection address, int port) {
    this.username = username;
    this.privateKey = privateKey;
    this.address = address;
    this.port = port;
  }

  @Override
  public String username() {
    return username;
  }

  @Override
  public SensitiveString credentials() {
    return privateKey;
  }

  @Override
  public Connection address() {
    return address;
  }

  @Override
  public int port() {
    return port;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PrivateKeyConnectionInfo that = (PrivateKeyConnectionInfo) o;
    return port == that.port
        && username.equals(that.username)
        && privateKey.equals(that.privateKey)
        && address.equals(that.address);
  }

  @Override
  public int hashCode() {
    int result = username.hashCode();
    result = 31 * result + privateKey.hashCode();
    result = 31 * result + address.hashCode();
    result = 31 * result + port;
    return result;
  }

  @Override
  public String toString() {
    return "PrivateKeyConnectionInfo{"
        + "username='"
        + username
        + '\''
        + ", privateKey="
        + privateKey
        + ", address="
        + address
        + ", port="
        + port
        + '}';
  }
}
