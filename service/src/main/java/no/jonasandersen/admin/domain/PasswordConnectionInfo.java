package no.jonasandersen.admin.domain;

import java.util.Objects;

public final class PasswordConnectionInfo implements ConnectionInfo {

  private final String username;
  private final SensitiveString password;
  private final Ip ip;
  private final int port;

  public PasswordConnectionInfo(String username, SensitiveString password, Ip ip, int port) {
    this.username = username;
    this.password = password;
    this.ip = ip;
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
  public Ip ip() {
    return ip;
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
    return Objects.equals(this.username, that.username) &&
        Objects.equals(this.password, that.password) &&
        Objects.equals(this.ip, that.ip) &&
        this.port == that.port;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, ip, port);
  }

  @Override
  public String toString() {
    return "ConnectionInfo[" +
        "username=" + username + ", " +
        "password=" + password + ", " +
        "ip=" + ip + ", " +
        "port=" + port + ']';
  }

}
