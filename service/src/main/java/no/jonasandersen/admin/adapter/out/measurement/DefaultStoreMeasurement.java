package no.jonasandersen.admin.adapter.out.measurement;

import no.jonasandersen.admin.application.port.StoreMeasurement;
import no.jonasandersen.admin.domain.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultStoreMeasurement implements StoreMeasurement {

  private static final Logger log = LoggerFactory.getLogger(DefaultStoreMeasurement.class);
  private final CrudMeasurementRepository repository;

  DefaultStoreMeasurement(CrudMeasurementRepository repository) {
    this.repository = repository;
  }

  @Override
  public void store(Measurement measurement) {
    MeasurementDbo dbo = new MeasurementDbo();
    dbo.setTemperature(measurement.celsius().value());
    dbo.setHumidity(measurement.humidity().value());
    dbo.setTimestamp(measurement.timestamp());
    MeasurementDbo saved = repository.save(dbo);
    log.info("Stored measurement: {}", saved);
  }
}
