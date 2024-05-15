package no.jonasandersen.admin.core.domain;

import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.domain.SensitiveString;

public record ConnectionInfo(String username, SensitiveString password, Ip ip, int port) {

  public static ConnectionInfo createNull() {
    return new ConnectionInfo("", SensitiveString.empty(), new Ip("0.0.0.0"), 0);
  }
}
