package no.jonasandersen.admin.infrastructure.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import no.jonasandersen.admin.application.UserService;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class DefaultOidcUserService extends OidcUserService {

  private static final Logger log = LoggerFactory.getLogger(DefaultOidcUserService.class);

  private final UserService userService;

  public DefaultOidcUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    User user = userService.storeOrLoadUser(oidcUser.getEmail());
    return new AdminOidcUser(oidcUser, user);
  }

  public record AdminOidcUser(OidcUser delegate, User user) implements OidcUser {

    @Override
    public Map<String, Object> getClaims() {
      return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
      return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
      return delegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
      return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      Set<GrantedAuthority> authorities = new HashSet<>(delegate.getAuthorities());

      for (Roles role : user.roles()) {
        authorities.add((GrantedAuthority) () -> "ROLE_" + role.name());
      }
      return Set.copyOf(authorities);
    }

    @Override
    public String getName() {
      return delegate.getName();
    }
  }
}
