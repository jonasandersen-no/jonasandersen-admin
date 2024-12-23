package no.jonasandersen.admin.adapter.out.measurement;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import no.jonasandersen.admin.application.port.StoreMeasurement;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.Measurement;
import no.jonasandersen.admin.domain.Measurement.Celsius;
import no.jonasandersen.admin.domain.Measurement.Humidity;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class DefaultStoreMeasurementTest extends IoBasedTest {

  @Autowired
  private CrudMeasurementRepository repository;

  @Autowired
  @Qualifier("storeMeasurement")
  private StoreMeasurement storeMeasurement;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldStoreMeasurement() {

    storeMeasurement.store(new Measurement(new Celsius("42.00"), new Humidity(10)));

    var measurements = repository.findAll();
    assertThat(measurements).hasSize(1);
    assertThat(measurements.getFirst().getHumidity()).isEqualTo(10);
    assertThat(measurements.getFirst().getTemperature()).isEqualTo("42.00");
    assertThat(measurements.getFirst().getTimestamp()).isNotNull();
  }
}