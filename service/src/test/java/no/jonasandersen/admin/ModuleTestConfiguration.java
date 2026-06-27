package no.jonasandersen.admin;

import java.util.Properties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class ModuleTestConfiguration {

  @Bean
  @ServiceConnection
  PostgreSQLContainer postgreSQLContainer() {
    return new PostgreSQLContainer(DockerImageName.parse("postgres:17"))
        .withDatabaseName("admin");
  }

  @Bean
  BuildProperties buildProperties() {
    Properties properties = new Properties();
    properties.put("version", "Intellij");
    return new BuildProperties(properties);
  }

}
