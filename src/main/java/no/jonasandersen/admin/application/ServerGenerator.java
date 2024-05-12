package no.jonasandersen.admin.application;

import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.domain.SensitiveString;

public class ServerGenerator {

  public enum ServerType {
    MINECRAFT
  }

  private final LinodeService service;
  private final SensitiveString defaultPassword;
  private final OutputListener<SensitiveString> outputListener = new OutputListener<>();

  public static ServerGenerator create(LinodeService service, SensitiveString defaultPassword) {
    return new ServerGenerator(service, defaultPassword);
  }

  public static ServerGenerator createNull() {
    return new ServerGenerator(LinodeService.createNull(), SensitiveString.of("Password123!"));
  }

  private ServerGenerator(LinodeService service, SensitiveString defaultPassword) {
    this.service = service;
    this.defaultPassword = defaultPassword;
  }

  public OutputTracker<SensitiveString> passwordTracker() {
    return outputListener.createTracker();
  }

  public ServerGeneratorResponse generate(ServerType serverType) {
    return generate(serverType, defaultPassword);
  }

  public ServerGeneratorResponse generate(ServerType serverType, SensitiveString password) {
    switch (serverType) {
      case MINECRAFT -> {
        outputListener.track(password);
        LinodeInstance instance = service.createDefaultMinecraftInstance(password);
        return ServerGeneratorResponse.from(instance);
      }
      default -> throw new IllegalStateException("Unexpected value: " + serverType);
    }
  }

}
