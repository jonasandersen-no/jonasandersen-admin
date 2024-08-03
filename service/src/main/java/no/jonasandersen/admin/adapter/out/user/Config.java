package no.jonasandersen.admin.adapter.out.user;

import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  UserSettingsRepository userSettingsRepository(CrudUserSettingsRepository repository) {
    return DefaultUserSettingsRepository.create(repository);
  }

  @Bean
  UserRepository userRepository(CrudUserDboRepository repository) {
    return DefaultUserRepository.create(repository);
  }
}
