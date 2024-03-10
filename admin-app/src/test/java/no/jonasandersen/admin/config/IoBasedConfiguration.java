package no.jonasandersen.admin.config;

import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.core.minecraft.port.TestServerApi;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class IoBasedConfiguration {

  @Bean
  ServerApi serverApi() {
    return new TestServerApi();
  }
}
