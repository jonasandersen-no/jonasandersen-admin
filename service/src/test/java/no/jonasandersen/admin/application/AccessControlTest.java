package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import no.jonasandersen.admin.application.port.InMemoryAccessControlRepository;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;

public class AccessControlTest {

  @Test
  void allowUser() {
    AccessControl accessControl = createAccessControl();

    User user = accessControl.allowUser("email@example.com");

    assertThat(user).isNotNull();
  }

  @Test
  void allowedUserHaveEmailAsUsername() {
    AccessControl accessControl = createAccessControl();

    User user = accessControl.allowUser("email@example.com");

    assertThat(user.username().value()).isEqualTo("email@example.com");
  }

  @Test
  void allowedUserHaveUserRole() {
    AccessControl accessControl = createAccessControl();

    User user = accessControl.allowUser("email@example.com");

    assertThat(user.roles()).containsExactly(Roles.USER);
  }

  @Test
  void userCanNotBeAddedTwice() {
    AccessControl accessControl = createAccessControl();

    accessControl.allowUser("email@example.com");

    assertThatThrownBy(() -> accessControl.allowUser("email@example.com"))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void retrieveSingleAllowedUser() {
    AccessControl accessControl = createAccessControl();
    accessControl.allowUser("email@example.com");

    List<User> users = accessControl.getAllowedUsers();

    assertThat(users).hasSize(1);
  }

  @Test
  void retrieveMultipleAllowedUsers() {
    AccessControl accessControl = createAccessControl();
    accessControl.allowUser("email@example.com");
    accessControl.allowUser("email2@example.com");

    List<User> users = accessControl.getAllowedUsers();

    assertThat(users).hasSize(2);
  }

  @Test
  void isUserAllowed() {
    AccessControl accessControl = createAccessControl();
    accessControl.allowUser("email@example.com");

    boolean allowed = accessControl.isUserAllowed("email@example.com");

    assertThat(allowed).isTrue();
  }

  @Test
  void userIsNotAllowed() {
    AccessControl accessControl = createAccessControl();
    accessControl.allowUser("email@example.com");

    boolean allowed = accessControl.isUserAllowed("email2@example.com");

    assertThat(allowed).isFalse();
  }

  private static AccessControl createAccessControl() {
    return new AccessControl(new InMemoryAccessControlRepository());
  }
}
