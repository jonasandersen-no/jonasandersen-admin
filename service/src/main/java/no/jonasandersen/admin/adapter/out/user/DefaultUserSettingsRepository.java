package no.jonasandersen.admin.adapter.out.user;

import java.util.Optional;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

class DefaultUserSettingsRepository implements UserSettingsRepository {

  private final Logger log = LoggerFactory.getLogger(DefaultUserSettingsRepository.class);
  private final CrudUserSettingsRepository repository;
  private final CrudUserDboRepository userRepository;

  public DefaultUserSettingsRepository(
      CrudUserSettingsRepository repository, CrudUserDboRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Theme> findTheme(Username username) {
    Optional<UserSettingsDbo> userSettingsDbo = repository.findByUsername(username.value());

    return userSettingsDbo.map(UserSettingsDbo::getTheme).map(Theme::new);
  }

  @Override
  @Transactional
  public void saveTheme(Username username, Theme theme) {
    Optional<UserSettingsDbo> settingsOpt = repository.findByUsername(username.value());

    if (settingsOpt.isPresent()) {
      UserSettingsDbo settings = settingsOpt.get();
      settings.setTheme(theme.value());
      repository.save(settings);
    } else {
      Optional<UserDbo> user = userRepository.findByUsername(username.value());
      if (user.isPresent()) {
        UserDbo userDbo = user.get();
        UserSettingsDbo settings = new UserSettingsDbo(userDbo, theme.value());
        repository.save(settings);
        return;
      }
      log.warn("User not found: {}", username);
    }
  }
}
