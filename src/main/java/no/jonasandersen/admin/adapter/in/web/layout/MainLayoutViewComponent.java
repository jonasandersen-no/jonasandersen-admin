package no.jonasandersen.admin.adapter.in.web.layout;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.context.SecurityContextHolder;

@ViewComponent
public class MainLayoutViewComponent {

  private final ThemeService themeService;
  private final BuildProperties buildProperties;

  public MainLayoutViewComponent(ThemeService themeService,
      BuildProperties buildProperties) {
    this.themeService = themeService;
    this.buildProperties = buildProperties;
  }

  public record MainLayoutView(String theme, String buildVersion, String title,
                               ViewContext mainContent) implements
      ViewContext {

  }

  public MainLayoutView render(String title, ViewContext mainContent) {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    Theme theme = themeService.findTheme(Username.create(name));
    return new MainLayoutView(theme.value(), buildProperties.getVersion(), title, mainContent);
  }
}
