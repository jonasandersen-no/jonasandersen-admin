package no.jonasandersen.admin.adapter.out.theme;

import java.util.Optional;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import no.jonasandersen.admin.core.theme.port.UserSettingsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
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

  @Override
  public void saveTheme(Username username, Theme theme) {
    Optional<UserSettingsDbo> settings = repository.findByUsername(username.value());

    if (settings.isPresent()) {
      UserSettingsDbo userSettingsDbo = settings.get();
      userSettingsDbo.setTheme(theme.value());
      repository.save(userSettingsDbo);
    } else {
      repository.save(new UserSettingsDbo(username.value(), theme.value()));
    }
  }
}
