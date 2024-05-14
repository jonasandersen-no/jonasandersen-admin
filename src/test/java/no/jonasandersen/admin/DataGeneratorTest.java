package no.jonasandersen.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.junit.jupiter.api.Test;

class DataGeneratorTest {


  @Test
  void onlyCreateInstancesWhenUsingLinodeStub() {
    UseStubPredicate useStubPredicate = new UseStubPredicate(
        new AdminProperties(null, null, null, "", Map.of("linode", true)));

    LinodeServerApi serverApi = LinodeServerApi.createNull();

    OutputTracker<LinodeInstanceApi> tracker = serverApi.track();

    DataGenerator generator = new DataGenerator(serverApi, useStubPredicate);
    generator.generate();

    assertThat(tracker.data()).hasSize(5);
  }

  @Test
  void noInstancesCreatedWhenNotUsingLinodeStub() {
    UseStubPredicate useStubPredicate = new UseStubPredicate(
        new AdminProperties(null, null, null, "", Map.of("linode", false)));

    LinodeServerApi serverApi = LinodeServerApi.createNull();

    OutputTracker<LinodeInstanceApi> tracker = serverApi.track();

    DataGenerator generator = new DataGenerator(serverApi, useStubPredicate);
    generator.generate();

    assertThat(tracker.data()).isEmpty();
  }
}