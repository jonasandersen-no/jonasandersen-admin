package no.jonasandersen.admin.adapter.out.measurement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Queue;
import no.jonasandersen.admin.application.port.StoreMeasurement;
import no.jonasandersen.admin.domain.Measurement;
import org.junit.jupiter.api.Test;

class AsyncStoreMeasurementTest {


  @Test
  void storingMeasurementAddsToQueue() {
    AsyncStoreMeasurement storeMeasurement = create();
    storeMeasurement.store(new Measurement("12345", "50"));

    Queue<Measurement> queue = storeMeasurement.getQueue();

    assertThat(queue).hasSize(1);
  }

  @Test
  void pullFromQueueAndStore() {
    AsyncStoreMeasurement storeMeasurement = create(true);
    storeMeasurement.store(new Measurement("12345", "50"));

    assertThat(storeMeasurement.getQueue()).hasSize(1);

    storeMeasurement.storeAsync();

    assertThat(storeMeasurement.getQueue()).isEmpty();
  }

  @Test
  void skipStoringIfNotConnectedToDatabase() {
    AsyncStoreMeasurement storeMeasurement = create();
    storeMeasurement.store(new Measurement("12345", "50"));

    assertThat(storeMeasurement.getQueue()).hasSize(1);

    storeMeasurement.storeAsync();

    assertThat(storeMeasurement.getQueue()).hasSize(1);
  }

  @Test
  void addBackToQueueIfStoringFails() {
    AsyncStoreMeasurement storeMeasurement = create(measurement -> {
      throw new RuntimeException("Failed to store");
    });

    storeMeasurement.store(new Measurement("12345", "50"));

    assertThat(storeMeasurement.getQueue()).hasSize(1);

    storeMeasurement.storeAsync();

    assertThat(storeMeasurement.getQueue()).hasSize(1);

  }

  private AsyncStoreMeasurement create() {
    return create(false);
  }

  private AsyncStoreMeasurement create(boolean isConnectedToDatabase) {
    return new AsyncStoreMeasurement(measurement -> {
    }, () -> isConnectedToDatabase);
  }

  private AsyncStoreMeasurement create(StoreMeasurement storeMeasurement) {
    return new AsyncStoreMeasurement(storeMeasurement, () -> true);
  }
}