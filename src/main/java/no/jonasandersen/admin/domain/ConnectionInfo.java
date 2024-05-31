package no.jonasandersen.admin.domain;

public record ConnectionInfo(String username, SensitiveString password, Ip ip, int port) {

  public static ConnectionInfo createNull() {
    return new ConnectionInfo("", SensitiveString.empty(), new Ip("0.0.0.0"), 0);
  }
}
