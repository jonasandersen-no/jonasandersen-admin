package no.jonasandersen.admin.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThemeService {

  public static final Theme DEFAULT_THEME = new Theme("light");
  private static final Logger log = LoggerFactory.getLogger(ThemeService.class);
  private final UserSettingsRepository userSettingsRepository;

  private ThemeService(UserSettingsRepository userSettingsRepository) {
    this.userSettingsRepository = userSettingsRepository;
  }

  public static ThemeService create(UserSettingsRepository userSettingsRepository) {
    return new ThemeService(userSettingsRepository);
  }

  public static ThemeService configureForTest() {
    return configureForTest(UnaryOperator.identity());
  }

  public static ThemeService configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());

    UserSettingsRepository repository =
        UserSettingsRepository.configureForTest(
            config1 -> {
              config.themes.forEach(config1::addTheme);
              return config1;
            });

    return new ThemeService(repository);
  }

  public Theme findTheme(Username username) {
    Optional<Theme> theme = userSettingsRepository.findTheme(username);

    if (theme.isPresent()) {
      log.info("Theme found for user: {}", username);
      return theme.get();
    }

    log.info("No theme found for user: {}", username);
    return DEFAULT_THEME;
  }

  public void saveTheme(Username username, Theme theme) {
    log.info("Setting theme for user: {} to: {}", username, theme);
    userSettingsRepository.saveTheme(username, theme);
  }

  public static class Config {

    private final Map<Username, Theme> themes = new HashMap<>();

    public Config addTheme(Username username, Theme theme) {
      themes.put(username, theme);
      return this;
    }
  }
}
