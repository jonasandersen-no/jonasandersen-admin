package no.jonasandersen.admin.adapter.out.measurement;

import jakarta.persistence.EntityManager;
import java.util.function.Supplier;
import no.jonasandersen.admin.application.port.StoreMeasurement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MeasurementConfig {

  @Bean
  StoreMeasurement storeMeasurement(CrudMeasurementRepository repository) {
    return new DefaultStoreMeasurement(repository);
  }

  @Bean
  @Primary
  StoreMeasurement asyncStoreMeasurement(StoreMeasurement storeMeasurement,
      Supplier<Boolean> checkDatabaseConnection) {
    return new AsyncStoreMeasurement(storeMeasurement, checkDatabaseConnection);
  }

  @Bean
  Supplier<Boolean> checkDatabaseConnection(EntityManager entityManager) {
    return new CheckDatabaseConnection(entityManager);
  }
}
