package no.jonasandersen.admin.user.infrastructure;

import no.jonasandersen.admin.user.AccessControl;
import no.jonasandersen.admin.user.application.DefaultAccessControl;
import no.jonasandersen.admin.user.application.ThemeService;
import no.jonasandersen.admin.user.application.port.AccessControlRepository;
import no.jonasandersen.admin.user.application.port.UserSettingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

  @Bean
  ThemeService themeService(UserSettingsRepository repository) {
    return ThemeService.create(repository);
  }

  @Bean
  AccessControl accessControl(AccessControlRepository accessControlRepository) {
    return new DefaultAccessControl(accessControlRepository);
  }
}
