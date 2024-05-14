package no.jonasandersen.admin;

import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.application.Feature;
import no.jonasandersen.admin.application.Features;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SensitiveString;
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

  private final LinodeServerApi linodeServerApi;

  public DataGenerator(ServerApi linodeServerApi) {
    this.linodeServerApi = (LinodeServerApi) linodeServerApi;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    generate();
  }

  void generate() {
    if (Features.isEnabled(Feature.LINODE_STUB)) {
      log.info("Creating {} instances", NUMBER_OF_INSTANCES);
      for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
        linodeServerApi.createInstance(InstanceDetails.createDefaultMinecraft(SensitiveString.of("password")));
      }
      return;
    }
    log.info("Not using stubs, skipping data generation.");
  }
}
