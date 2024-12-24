package no.jonasandersen.admin.adapter.out.measurement;

import jakarta.persistence.EntityManager;
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
      CheckDatabaseConnection checkDatabaseConnection) {
    return new AsyncStoreMeasurement(storeMeasurement, checkDatabaseConnection);
  }

  @Bean
  CheckDatabaseConnection checkDatabaseConnection(EntityManager entityManager) {
    return new DeaultCheckDatabaseConnection(entityManager);
  }
}
