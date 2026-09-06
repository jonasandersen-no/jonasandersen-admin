package no.jonasandersen.admin.user.adapter;

import no.jonasandersen.admin.user.domain.Username;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsernameResolver {

  private UsernameResolver() {}

  public static Username getUsername() {
    return Username.create(getUsernameAsString());
  }

  public static String getUsernameAsString() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getName();
    }
    return "unknown";
  }
}
