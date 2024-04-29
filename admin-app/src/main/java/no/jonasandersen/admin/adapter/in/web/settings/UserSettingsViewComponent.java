package no.jonasandersen.admin.adapter.in.web.settings;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.theme.UserSettingsService;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import org.springframework.security.core.context.SecurityContextHolder;

@ViewComponent
public class UserSettingsViewComponent {

  private final UserSettingsService userSettingsService;

  UserSettingsViewComponent(UserSettingsService userSettingsService) {
    this.userSettingsService = userSettingsService;
  }

  public record UserSettingsView(String theme) implements ViewContext {

  }

  public UserSettingsView render() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    Theme theme = userSettingsService.getTheme(Username.from(name));

    return new UserSettingsView(theme.value());
  }
}
