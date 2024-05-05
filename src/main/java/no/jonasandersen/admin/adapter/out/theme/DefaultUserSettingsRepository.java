package no.jonasandersen.admin.adapter.out.theme;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import no.jonasandersen.admin.application.port.UserSettingsRepository;

public class DefaultUserSettingsRepository implements UserSettingsRepository {

  private final Jdbc repository;

  private DefaultUserSettingsRepository(Jdbc repository) {
    this.repository = repository;
  }

  public static DefaultUserSettingsRepository create(CrudUserSettingsRepository repository) {
    return new DefaultUserSettingsRepository(new RealJdbc(repository));
  }

  public static DefaultUserSettingsRepository createNull(UserSettingsDbo... defaultSettings) {
    return new DefaultUserSettingsRepository(new StubJdbc(defaultSettings));
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

  public interface Jdbc {

    Optional<UserSettingsDbo> findByUsername(String value);

    UserSettingsDbo save(UserSettingsDbo userSettingsDbo);
  }

  private record RealJdbc(CrudUserSettingsRepository repository) implements Jdbc {

    @Override
    public Optional<UserSettingsDbo> findByUsername(String value) {
      return repository.findByUsername(value);
    }

    @Override
    public UserSettingsDbo save(UserSettingsDbo userSettingsDbo) {
      return repository.save(userSettingsDbo);
    }
  }

  private static class StubJdbc implements Jdbc {

    private final Map<String, UserSettingsDbo> settings = new HashMap<>();

    public StubJdbc(UserSettingsDbo... defaultSettings) {
      for (UserSettingsDbo defaultSetting : defaultSettings) {
        settings.put(defaultSetting.getUsername(), defaultSetting);
      }
    }

    @Override
    public Optional<UserSettingsDbo> findByUsername(String value) {
      return Optional.ofNullable(settings.get(value));
    }

    @Override
    public UserSettingsDbo save(UserSettingsDbo userSettingsDbo) {
      settings.put(userSettingsDbo.getUsername(), userSettingsDbo);
      return userSettingsDbo;
    }
  }
}
