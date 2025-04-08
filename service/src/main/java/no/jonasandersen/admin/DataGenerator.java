package no.jonasandersen.admin;

import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.infrastructure.Features;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataGenerator implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);
  public static final int NUMBER_OF_INSTANCES = 5;

  private final ServerGenerator serverGenerator;

  public DataGenerator(ServerGenerator serverGenerator) {
    this.serverGenerator = serverGenerator;
  }

  @Override
  public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
    generate();
  }

  void generate() {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      log.info("Creating {} instances", NUMBER_OF_INSTANCES);
      for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
        serverGenerator.generate("generated", SensitiveString.of("password"), ServerType.MINECRAFT);
      }
      return;
    }
    log.debug("Not using stubs, skipping data generation.");
  }
}
