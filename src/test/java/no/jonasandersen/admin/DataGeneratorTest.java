package no.jonasandersen.admin;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.Feature;
import no.jonasandersen.admin.application.Features;
import org.junit.jupiter.api.Test;

class DataGeneratorTest {

  @Test
  void onlyCreateInstancesWhenUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, true);

    LinodeServerApi serverApi = LinodeServerApi.createNull();

    OutputTracker<LinodeInstanceApi> tracker = serverApi.track();

    DataGenerator generator = new DataGenerator(serverApi);
    generator.generate();

    assertThat(tracker.data()).hasSize(5);
  }

  @Test
  void noInstancesCreatedWhenNotUsingLinodeStub() {
    Features.setFeature(Feature.LINODE_STUB, false);

    LinodeServerApi serverApi = LinodeServerApi.createNull();

    OutputTracker<LinodeInstanceApi> tracker = serverApi.track();

    DataGenerator generator = new DataGenerator(serverApi);
    generator.generate();

    assertThat(tracker.data()).isEmpty();
  }
}