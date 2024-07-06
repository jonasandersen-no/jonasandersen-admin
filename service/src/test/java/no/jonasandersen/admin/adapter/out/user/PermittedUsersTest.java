package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class PermittedUsersTest extends IoBasedTest {

  @Autowired
  private PermittedUsers permittedUsers;

  @Autowired
  private JdbcClient jdbcClient;

  @Test
  void userIsAllowedWhenInPermittedList() {
    jdbcClient.sql("insert into permitted_users (subject, email) values (?, ?)")
        .params("12345678", "some@email.com")
        .update();

    boolean allowed = permittedUsers.isAllowed("12345678", "some@email.com");

    assertThat(allowed).isTrue();
  }
}