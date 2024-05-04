package no.jonasandersen.admin.adapter.in.web.layout;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.theme.UserSettingsService;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.context.SecurityContextHolder;

@ViewComponent
public class MainLayoutViewComponent {

  private final UserSettingsService userSettingsService;
  private final BuildProperties buildProperties;

  public MainLayoutViewComponent(UserSettingsService userSettingsService,
      BuildProperties buildProperties) {
    this.userSettingsService = userSettingsService;
    this.buildProperties = buildProperties;
  }

  public record MainLayoutView(String theme, String buildVersion, String title,
                               ViewContext mainContent) implements
      ViewContext {

  }

  public MainLayoutView render(String title, ViewContext mainContent) {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    Theme theme = userSettingsService.getTheme(Username.from(name));
    return new MainLayoutView(theme.value(), buildProperties.getVersion(), title, mainContent);
  }
}
