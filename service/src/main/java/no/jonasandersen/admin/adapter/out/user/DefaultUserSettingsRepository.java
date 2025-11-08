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
  private final CrudUserDboRepository userRepository;

  public DefaultUserSettingsRepository(CrudUserDboRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Theme> findTheme(Username username) {
    Optional<UserDbo> user = userRepository.findByUsername(username.value());
    if (user.isPresent()) {
      UserSettingsDbo settings = user.get().getSettings();
      if (settings != null) {
        return Optional.of(settings.getTheme()).map(Theme::new);
      }
    }
    return Optional.empty();
  }

  @Override
  @Transactional
  public void saveTheme(Username username, Theme theme) {
    Optional<UserDbo> user = userRepository.findByUsername(username.value());
    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not found: " + username);
    }
    Optional<UserSettingsDbo> settingsOpt = Optional.ofNullable(user.get().getSettings());

    if (settingsOpt.isPresent()) {
      UserSettingsDbo settings = settingsOpt.get();
      settings.setTheme(theme.value());
      user.get().setSettings(settings);
      userRepository.save(user.get());
    } else {
      UserSettingsDbo settings = new UserSettingsDbo(theme.value());
      user.get().setSettings(settings);
      userRepository.save(user.get());
      return;
    }
    log.warn("User not found: {}", username);
  }
}
