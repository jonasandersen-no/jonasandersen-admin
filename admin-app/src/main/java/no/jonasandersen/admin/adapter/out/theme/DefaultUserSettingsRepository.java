package no.jonasandersen.admin.adapter.out.theme;

import java.util.Optional;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import no.jonasandersen.admin.core.theme.port.UserSettingsRepository;
import org.springframework.stereotype.Component;

@Component
class DefaultUserSettingsRepository implements UserSettingsRepository {

  private final JdbcUserSettingsRepository repository;

  DefaultUserSettingsRepository(JdbcUserSettingsRepository repository) {
    this.repository = repository;
  }


  @Override
  public Optional<Theme> findTheme(Username username) {
    Optional<UserSettingsDbo> userSettingsDbo = repository.findByUsername(username.value());

    return userSettingsDbo.map(UserSettingsDbo::getTheme)
        .map(Theme::new);
  }
}
