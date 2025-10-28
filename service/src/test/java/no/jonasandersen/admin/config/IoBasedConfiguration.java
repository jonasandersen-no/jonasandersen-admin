package no.jonasandersen.admin.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class IoBasedConfiguration {

  @Bean
  @ServiceConnection
  PostgreSQLContainer postgreSQLContainer() {
    return new PostgreSQLContainer(DockerImageName.parse("postgres:17"))
        .withDatabaseName("admin");
  }

  @Bean
  @ServiceConnection
  MongoDBContainer mongoDBContainer() {
    return new MongoDBContainer(DockerImageName.parse("mongodb/mongodb-community-server:latest"));
  }
}
