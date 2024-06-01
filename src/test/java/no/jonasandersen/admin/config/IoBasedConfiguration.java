package no.jonasandersen.admin.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
class IoBasedConfiguration {

  @Bean
  @ServiceConnection
  MariaDBContainer<?> mariaDBContainer() {
    return new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"))
        .withDatabaseName("admin");
  }

}