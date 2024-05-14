package no.jonasandersen.admin;

import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SensitiveString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataGenerator implements ApplicationListener<ApplicationStartedEvent> {

  private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

  private final LinodeServerApi linodeServerApi;
  private final UseStubPredicate useStubPredicate;

  public DataGenerator(ServerApi linodeServerApi,
      UseStubPredicate useStubPredicate) {
    this.linodeServerApi = (LinodeServerApi) linodeServerApi;
    this.useStubPredicate = useStubPredicate;
  }

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    generate();
  }

  void generate() {
    if (useStubPredicate.test("linode")) {
      log.info("Creating 5 instances");
      for (int i = 0; i < 5; i++) {
        linodeServerApi.createInstance(InstanceDetails.createDefaultMinecraft(SensitiveString.of("password")));
      }
      return;
    }
    log.info("Not using stubs, skipping data generation.");
  }
}
