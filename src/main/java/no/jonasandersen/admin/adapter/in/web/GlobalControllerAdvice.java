package no.jonasandersen.admin.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "no.jonasandersen.admin.adapter.in.web")
public class GlobalControllerAdvice {

  private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
  private final BuildProperties buildProperties;
  private final ThemeService themeService;

  public GlobalControllerAdvice(BuildProperties buildProperties, ThemeService themeService) {
    this.buildProperties = buildProperties;
    this.themeService = themeService;
  }

  @ModelAttribute("loggedInUser")
  public String getLoggedInUser() {
    return UsernameResolver.getUsername();
  }

  @ModelAttribute("currentStoredTheme")
  public String getTheme() {
    return themeService.findTheme(Username.create(getLoggedInUser())).value();
  }

  @ModelAttribute("buildVersion")
  public String getBuildVersion() {
    return buildProperties.getVersion();
  }

  @ModelAttribute("currentPath")
  public String getCurrentPath(HttpServletRequest request) {
    log.info("Request URI: {}", request.getRequestURI());
    return request.getRequestURI();
  }
}
