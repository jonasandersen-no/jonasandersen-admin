package no.jonasandersen.admin.application.port;

import java.util.Optional;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;

public interface UserSettingsRepository {

  Optional<Theme> findTheme(Username username);

  void saveTheme(Username username, Theme theme);
}
