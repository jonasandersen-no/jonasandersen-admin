package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;
import org.junit.jupiter.api.Test;

class DefaultUserServiceTest {

  @Test
  void newUserIsStoredAsUser() {
    User user = DefaultUserService.configureForTest().storeOrLoadUser("username");
    assertThat(user).extracting(User::username, User::roles)
        .containsExactly(Username.create("username"), Set.of(Roles.USER));
  }

  @Test
  void existingUserIsLoaded() {
    DefaultUserService userService = DefaultUserService.configureForTest(config -> config.addUsers(createAdminUser()));
    User loadedUser = userService.storeOrLoadUser("admin");
    assertThat(loadedUser).extracting(User::username, User::roles)
        .containsExactly(Username.create("admin"), Set.of(Roles.ADMIN));
  }

  User createAdminUser() {
    return new User(Username.create("admin"), Set.of(Roles.ADMIN));
  }
}