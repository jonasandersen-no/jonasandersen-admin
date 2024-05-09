package no.jonasandersen.admin.application;

import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.LinodeService;

public class ServerGenerator {

  public enum ServerType {
    MINECRAFT
  }

  private final LinodeService service;

  public static ServerGenerator create(LinodeService service) {
    return new ServerGenerator(service);
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(LinodeService.createNull());
  }

  private ServerGenerator(LinodeService service) {
    this.service = service;
  }

  public ServerGeneratorResponse generate(ServerType serverType) {

    switch (serverType) {
      case MINECRAFT -> {
        LinodeInstance instance = service.createDefaultMinecraftInstance();
        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

}
