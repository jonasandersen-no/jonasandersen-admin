package no.jonasandersen.admin.adapter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

public class UsernameResolver {

  public static String getUsername() {
    String result = "unknown";
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken token
        && token.getPrincipal() instanceof DefaultOidcUser user) {
      result = user.getEmail();
    }
    return result;
  }
}