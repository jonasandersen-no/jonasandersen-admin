package no.jonasandersen.admin.core.theme.port;

import java.util.Optional;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;

public interface UserSettingsRepository {

  Optional<Theme> findTheme(Username username);
}
