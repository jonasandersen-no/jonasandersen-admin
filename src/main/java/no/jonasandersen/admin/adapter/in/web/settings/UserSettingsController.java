package no.jonasandersen.admin.adapter.in.web.settings;

import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
class UserSettingsController {

  private final MainLayoutViewComponent mainLayoutViewComponent;
  private final UserSettingsViewComponent userSettingsViewComponent;
  private final ThemeService themeService;

  UserSettingsController(MainLayoutViewComponent mainLayoutViewComponent,
      UserSettingsViewComponent userSettingsViewComponent,
      ThemeService themeService) {
    this.mainLayoutViewComponent = mainLayoutViewComponent;
    this.userSettingsViewComponent = userSettingsViewComponent;
    this.themeService = themeService;
  }

  @GetMapping
  public ViewContext settings() {
    return mainLayoutViewComponent.render("Settings", userSettingsViewComponent.render());
  }

  @PostMapping
  public ViewContext saveSettings(@RequestParam String theme) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();

    themeService.saveTheme(Username.create(userName), Theme.from(theme));

    return mainLayoutViewComponent.render("Settings", userSettingsViewComponent.render());
  }
}
