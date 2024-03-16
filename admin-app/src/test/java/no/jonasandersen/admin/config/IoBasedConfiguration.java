package no.jonasandersen.admin.config;

import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
class IoBasedConfiguration {

  @Bean
  ServerApi serverApi() {
    return new TestServerApi();
  }

  @Bean
  @ServiceConnection
  @SuppressWarnings("resource")
  MariaDBContainer<?> mariaDBContainer() {
    return new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"))
        .withDatabaseName("admin");
  }
}