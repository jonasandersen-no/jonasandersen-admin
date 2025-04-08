package no.jonasandersen.admin.domain;

public final class PrivateKeyConnectionInfo implements ConnectionInfo {

  private final String username;
  private final SensitiveString privateKey;
  private final Ip ip;
  private final int port;

  public PrivateKeyConnectionInfo(String username, SensitiveString privateKey, Ip ip, int port) {
    this.username = username;
    this.privateKey = privateKey;
    this.ip = ip;
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
  public Ip ip() {
    return ip;
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
        && ip.equals(that.ip);
  }

  @Override
  public int hashCode() {
    int result = username.hashCode();
    result = 31 * result + privateKey.hashCode();
    result = 31 * result + ip.hashCode();
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
        + ", ip="
        + ip
        + ", port="
        + port
        + '}';
  }
}
