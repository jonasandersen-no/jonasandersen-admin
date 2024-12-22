package no.jonasandersen.admin.adapter.out.measurement;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import no.jonasandersen.admin.application.port.StoreMeasurement;
import no.jonasandersen.admin.domain.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

class AsyncStoreMeasurement implements StoreMeasurement {

  private static final Logger log = LoggerFactory.getLogger(AsyncStoreMeasurement.class);
  private final Queue<Measurement> measurements = new ConcurrentLinkedQueue<>();
  private final StoreMeasurement delegate;
  private final Supplier<Boolean> checkDatabaseConnection;

  AsyncStoreMeasurement(StoreMeasurement delegate,
      Supplier<Boolean> checkDatabaseConnection) {
    this.delegate = delegate;
    this.checkDatabaseConnection = checkDatabaseConnection;
  }

  @Override
  public void store(Measurement measurement) {
    measurements.add(measurement);
  }

  @Scheduled(cron = "0 * * * * *")
  void storeAsync() {

    if (!checkDatabaseConnection.get()) {
      return;
    }

    log.info(" Storing {} measurements", measurements.size());
    Set<Measurement> failedMeasurements = new HashSet<>();

    while (!measurements.isEmpty()) {
      Measurement measurement = measurements.poll();
      try {
        delegate.store(measurement);
      } catch (Exception e) {
        log.error("Something went wrong when storing measurement: {}. Adding measurement back to queue", measurement, e);
        failedMeasurements.add(measurement);
      }
    }

    measurements.addAll(failedMeasurements);
  }

  public Queue<Measurement> getQueue() {
    return measurements;
  }

}
