package no.jonasandersen.admin;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.linode.LinodeInstanceDatabaseRepository;
import no.jonasandersen.admin.application.Feature;
import no.jonasandersen.admin.application.Features;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import org.junit.jupiter.api.Test;

class DataGeneratorTest {

  @Test
  void onlyCreateInstancesWhenUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, true);

    ServerGenerator serverGenerator = ServerGenerator.createNull();

    OutputTracker<LinodeInstance> tracker = serverGenerator.instanceTracker();

    DataGenerator generator = new DataGenerator(serverGenerator, LinodeInstanceDatabaseRepository.createNull());
    generator.generate();

    assertThat(tracker.data()).hasSize(5);
  }

  @Test
  void noInstancesCreatedWhenNotUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, false);

    ServerGenerator serverGenerator = ServerGenerator.createNull();

    OutputTracker<LinodeInstance> tracker = serverGenerator.instanceTracker();

    DataGenerator generator = new DataGenerator(serverGenerator, LinodeInstanceDatabaseRepository.createNull());
    generator.generate();

    assertThat(tracker.data()).isEmpty();
  }
}