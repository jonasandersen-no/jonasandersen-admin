package no.jonasandersen.admin.adapter.in.web.settings;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.springframework.security.core.context.SecurityContextHolder;

@ViewComponent
public class UserSettingsViewComponent {

  private final ThemeService themeService;

  UserSettingsViewComponent(ThemeService themeService) {
    this.themeService = themeService;
  }

  public record UserSettingsView(String theme) implements ViewContext {

  }

  public UserSettingsView render() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    Theme theme = themeService.findTheme(Username.create(name));

    return new UserSettingsView(theme.value());
  }
}
