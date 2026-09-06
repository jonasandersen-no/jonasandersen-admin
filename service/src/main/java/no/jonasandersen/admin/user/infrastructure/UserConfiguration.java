package no.jonasandersen.admin.user.infrastructure;

import no.jonasandersen.admin.user.application.ThemeService;
import no.jonasandersen.admin.user.application.port.UserSettingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
  }
}
