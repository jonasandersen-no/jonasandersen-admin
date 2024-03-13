package no.jonasandersen.admin;

import java.util.Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuildInfoConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public BuildProperties buildProperties() {
    Properties properties = new Properties();
    properties.put("version", "Intellij");
    return new BuildProperties(properties);
  }
}
