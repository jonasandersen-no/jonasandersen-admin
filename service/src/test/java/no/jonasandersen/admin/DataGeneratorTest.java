package no.jonasandersen.admin;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.infrastructure.Features;
import org.junit.jupiter.api.Test;

class DataGeneratorTest {

  @Test
  void onlyCreateInstancesWhenUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, true);

    ServerGenerator serverGenerator = ServerGenerator.createNull();

    OutputTracker<LinodeInstance> tracker = serverGenerator.instanceTracker();

    DataGenerator generator = new DataGenerator(serverGenerator);
    generator.generate();

    assertThat(tracker.data()).hasSize(5);
  }

  @Test
  void noInstancesCreatedWhenNotUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, false);

    ServerGenerator serverGenerator = ServerGenerator.createNull();

    OutputTracker<LinodeInstance> tracker = serverGenerator.instanceTracker();

    DataGenerator generator = new DataGenerator(serverGenerator);
    generator.generate();

    assertThat(tracker.data()).isEmpty();
  }
}