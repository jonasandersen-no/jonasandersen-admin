package no.jonasandersen.admin.application;

import no.jonasandersen.admin.core.minecraft.domain.Ip;

public class ServerGenerator {


  public static ServerGenerator create() {
    return new ServerGenerator();
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator();
  }

  private ServerGenerator() {
  }

  public ServerGeneratorResponse generate() {
    return new ServerGeneratorResponse("label", new Ip("127.0.0.1"));
  }
}
