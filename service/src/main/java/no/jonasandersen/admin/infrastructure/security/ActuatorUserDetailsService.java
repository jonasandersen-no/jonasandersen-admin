package no.jonasandersen.admin.infrastructure.security;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import no.jonasandersen.admin.infrastructure.AdminProperties.Actuator;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ActuatorUserDetailsService implements UserDetailsService {

  private final String encryptedPassword;
  private final @NotNull String username;

  public ActuatorUserDetailsService(AdminProperties properties) {
    Actuator actuator = properties.actuator();
    username = actuator.username();
    String password = actuator.password();
    encryptedPassword = password.replace("'", "");
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (this.username.equals(username)) {
      return new User(this.username, encryptedPassword, List.of(() -> "ROLE_ACTUATOR"));
    }
    throw new UsernameNotFoundException("Not a valid actuator user");
  }
}
