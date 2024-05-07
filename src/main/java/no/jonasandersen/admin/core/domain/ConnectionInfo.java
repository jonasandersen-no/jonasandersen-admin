package no.jonasandersen.admin.core.domain;

import no.jonasandersen.admin.core.minecraft.domain.Ip;

public record ConnectionInfo(String username, String password, Ip ip, int port) {

  public static ConnectionInfo createNull() {
    return new ConnectionInfo("", "", new Ip("0.0.0.0"), 0);
  }
}
