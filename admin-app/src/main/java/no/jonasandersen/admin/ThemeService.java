package no.jonasandersen.admin;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

  private static final Logger log = LoggerFactory.getLogger(ThemeService.class);
  public static String theme = "light";

  private static final Map<Object, String> themes = new HashMap<>();

  public ThemeService(AdminProperties adminProperties) {
    theme = adminProperties.defaultTheme();
  }


  @EventListener
  public void onSuccess(AuthenticationSuccessEvent success) {
    Object principal = success.getAuthentication().getPrincipal();

    log.info("Setting theme to dark for user: {}", principal);
    themes.put(principal, "dark");
  }

  public static String getTheme() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Getting theme for user: {}", principal);
    return themes.getOrDefault(principal, theme);
  }

}
