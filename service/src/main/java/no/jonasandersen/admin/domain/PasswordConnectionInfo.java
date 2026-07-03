package no.jonasandersen.admin.domain;

import java.util.Objects;

public final class PasswordConnectionInfo implements ConnectionInfo {

  private final String username;
  private final SensitiveString password;
  private final Connection address;
  private final int port;

  public PasswordConnectionInfo(String username, SensitiveString password, Connection address, int port) {
    this.username = username;
    this.password = password;
    this.address = address;
    this.port = port;
  }

  public static PasswordConnectionInfo createNull() {
    return new PasswordConnectionInfo("", SensitiveString.empty(), new Ip("0.0.0.0"), 0);
  }

  @Override
  public String username() {
    return username;
  }

  @Override
  public SensitiveString credentials() {
    return password();
  }

  public SensitiveString password() {
    return password;
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
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (PasswordConnectionInfo) obj;
    return Objects.equals(this.username, that.username)
        && Objects.equals(this.password, that.password)
        && Objects.equals(this.address, that.address)
        && this.port == that.port;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, address, port);
  }

  @Override
  public String toString() {
    return "ConnectionInfo["
           + "username="
           + username
           + ", "
           + "password="
           + password
           + ", "
           + "address="
           + address
           + ", "
           + "port="
           + port
           + ']';
  }
}
