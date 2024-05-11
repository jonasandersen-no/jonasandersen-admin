package no.jonasandersen.admin.infrastructure;

import java.util.Properties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"intellij", "integration"})
class BuildInfoConfiguration {

  @Bean
  BuildProperties buildProperties() {
    Properties properties = new Properties();
    properties.put("version", "Intellij");
    return new BuildProperties(properties);
  }
}
