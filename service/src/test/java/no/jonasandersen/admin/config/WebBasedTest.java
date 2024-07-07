package no.jonasandersen.admin.config;

import java.util.Properties;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@Tag("integration")
@Import(WebBasedTest.TestConfig.class)
@EnableConfigurationProperties(AdminProperties.class)
public abstract class WebBasedTest {

  @TestConfiguration
  static class TestConfig {

    @Bean
    BuildProperties buildProperties() {
      return new BuildProperties(new Properties());
    }

    @Bean
    ThemeService themeService() {
      return ThemeService.createNull();
    }
  }
}


