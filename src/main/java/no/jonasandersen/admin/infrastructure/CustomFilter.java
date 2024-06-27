package no.jonasandersen.admin.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import no.jonasandersen.admin.adapter.out.user.PermittedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(CustomFilter.class);

  private final PermittedUsers permittedUsers;

  public CustomFilter(PermittedUsers permittedUsers) {
    this.permittedUsers = permittedUsers;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken token
        && token.getPrincipal() instanceof DefaultOidcUser user) {
      String subject = user.getSubject();
      String email = user.getEmail();

      if (!permittedUsers.isAllowed(subject, email)) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
