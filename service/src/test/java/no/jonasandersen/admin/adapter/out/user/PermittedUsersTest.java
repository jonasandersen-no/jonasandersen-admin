package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PermittedUsersTest extends IoBasedTest {

  @Autowired
  private PermittedUsers permittedUsers;

  @Autowired
  private CrudPermittedUserRepository repository;

  @Test
  void userIsAllowedWhenInPermittedList() {

    PermittedUserDbo user = new PermittedUserDbo();
    user.setSubject("12345678");
    user.setEmail("some@email.com");
    repository.save(user);

    boolean allowed = permittedUsers.isAllowed("12345678", "some@email.com");

    assertThat(allowed).isTrue();
  }
}