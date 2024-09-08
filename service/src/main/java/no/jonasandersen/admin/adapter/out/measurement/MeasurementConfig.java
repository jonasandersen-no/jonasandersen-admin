package no.jonasandersen.admin.adapter.out.measurement;

import no.jonasandersen.admin.application.port.StoreMeasurement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeasurementConfig {

  @Bean
  StoreMeasurement storeMeasurement(CrudMeasurementRepository repository) {
    return new DefaultStoreMeasurement(repository);
  }
}
