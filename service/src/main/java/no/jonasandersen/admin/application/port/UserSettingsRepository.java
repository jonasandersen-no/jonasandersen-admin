package no.jonasandersen.admin.application.port;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;

public interface UserSettingsRepository {

  Optional<Theme> findTheme(Username username);

  void saveTheme(Username username, Theme theme);

  static UserSettingsRepository configureForTest() {
    return configureForTest(UnaryOperator.identity());
  }

  static UserSettingsRepository configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());

    InMemoryUserRepository repository = new InMemoryUserRepository();
    config.themes.forEach(repository::saveTheme);
    return repository;
  }

  class Config {

    Map<Username, Theme> themes = new HashMap<>();

    public void addTheme(Username username, Theme theme) {
      themes.put(username, theme);
    }
  }

  class InMemoryUserRepository implements UserSettingsRepository {

    public final Map<Username, Theme> themes = new HashMap<>();

    @Override
    public Optional<Theme> findTheme(Username username) {
      return Optional.ofNullable(themes.get(username));
    }

    @Override
    public void saveTheme(Username username, Theme theme) {
      themes.put(username, theme);
    }
  }
}
