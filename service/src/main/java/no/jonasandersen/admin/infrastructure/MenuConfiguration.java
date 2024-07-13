package no.jonasandersen.admin.infrastructure;

import no.jonasandersen.admin.application.MenuService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuConfiguration {

  @Bean
  MenuService menuService() {
    return new MenuService();
  }
}
