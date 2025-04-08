package no.jonasandersen.admin.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import no.jonasandersen.admin.application.AccessControl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.filter.OncePerRequestFilter;

class PermittedUserFilter extends OncePerRequestFilter {

  private final AccessControl accessControl;

  public PermittedUserFilter(AccessControl accessControl) {
    this.accessControl = accessControl;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken token
        && token.getPrincipal() instanceof OidcUser user) {
      String email = user.getEmail();

      if (!accessControl.isUserAllowed(email)) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
