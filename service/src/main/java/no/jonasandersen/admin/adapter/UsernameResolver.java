package no.jonasandersen.admin.adapter;

import no.jonasandersen.admin.domain.Username;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class UsernameResolver {

  private UsernameResolver() {
  }

  public static Username getUsername() {
    return Username.create(getUsernameAsString());
  }

  public static String getUsernameAsString() {
    String result = "unknown";
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken token
        && token.getPrincipal() instanceof OidcUser user) {
      result = user.getEmail();
    }
    return result;
  }
}