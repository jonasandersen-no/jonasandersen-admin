package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Username;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "no.jonasandersen.admin.adapter.in.web")
public class GlobalControllerAdvice {

  private final BuildProperties buildProperties;
  private final ThemeService themeService;

  public GlobalControllerAdvice(BuildProperties buildProperties, ThemeService themeService) {
    this.buildProperties = buildProperties;
    this.themeService = themeService;
  }

  @ModelAttribute("loggedInUser")
  public String getLoggedInUser() {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    return "unknown";
  }

  @ModelAttribute("currentStoredTheme")
  public String getTheme() {
    return themeService.findTheme(Username.create(getLoggedInUser())).value();
  }

  @ModelAttribute("buildVersion")
  public String getBuildVersion() {
    return buildProperties.getVersion();
  }
}
