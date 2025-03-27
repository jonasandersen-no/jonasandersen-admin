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
      UserDbo found = user.get();

      if (found.getSettings() != null) {
        String theme = found.getSettings().getTheme();
        return Optional.of(Theme.from(theme));
      }
    }

    return Optional.empty();
  }

  @Override
  @Transactional
  public void saveTheme(Username username, Theme theme) {
    Optional<UserDbo> userOpt = userRepository.findByUsername(username.value());

    if (userOpt.isEmpty()) {
      log.warn("No user found for username {}", username.value());
      return;
    }
    UserDbo found = userOpt.get();
    if (found.getSettings() == null) {
      UserSettingsDbo settings = new UserSettingsDbo(found, theme.value());
      found.setSettings(settings);
      found.getSettings().setTheme(theme.value());
      userRepository.save(found);
      return;
    }

    UserSettingsDbo settings = found.getSettings();
    settings.setTheme(theme.value());
    userRepository.save(found);
  }

}
