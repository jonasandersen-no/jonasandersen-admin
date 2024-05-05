package no.jonasandersen.admin.core.theme;

import java.util.List;
import java.util.Map;
import no.jonasandersen.admin.adapter.out.theme.DefaultUserSettingsRepository;
import no.jonasandersen.admin.adapter.out.theme.UserSettingsDbo;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import no.jonasandersen.admin.core.theme.port.UserSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSettingsService {

  public static final Theme DEFAULT_THEME = new Theme("light");
  private static final Logger log = LoggerFactory.getLogger(UserSettingsService.class);
  private final UserSettingsRepository userSettingsRepository;

  private UserSettingsService(UserSettingsRepository userSettingsRepository) {
    this.userSettingsRepository = userSettingsRepository;
  }

  public static UserSettingsService create(UserSettingsRepository userSettingsRepository) {
    return new UserSettingsService(userSettingsRepository);
  }

  public static UserSettingsService createNull(UserSettingsDbo... defaultSettings) {
    return new UserSettingsService(DefaultUserSettingsRepository.createNull(defaultSettings));
  }

  public Theme getTheme(Username username) {

    return userSettingsRepository.findTheme(username).orElseGet(() -> {
      log.info("No theme found for user: {}", username);
      return DEFAULT_THEME;
    });
  }

  public void setTheme(Username username, Theme theme) {
    log.info("Setting theme for user: {} to: {}", username, theme);
    userSettingsRepository.saveTheme(username, theme);
  }
}
