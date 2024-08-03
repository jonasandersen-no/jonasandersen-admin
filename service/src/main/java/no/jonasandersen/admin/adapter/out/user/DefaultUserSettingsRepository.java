package no.jonasandersen.admin.adapter.out.user;

import java.util.Optional;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;

class DefaultUserSettingsRepository implements UserSettingsRepository {

  private final CrudUserSettingsRepository repository;

  private DefaultUserSettingsRepository(CrudUserSettingsRepository repository) {
    this.repository = repository;
  }

  public static DefaultUserSettingsRepository create(CrudUserSettingsRepository repository) {
    return new DefaultUserSettingsRepository(repository);
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
