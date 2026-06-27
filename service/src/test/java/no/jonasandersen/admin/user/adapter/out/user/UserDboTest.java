package no.jonasandersen.admin.user.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import no.jonasandersen.admin.ModuleTest;
import no.jonasandersen.admin.user.domain.Username;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

@Disabled
@ModuleTest
class UserDboTest {

  @Autowired private CrudUserDboRepository crudUserDboRepository;

  @Autowired private JdbcClient jdbcClient;

  @BeforeEach
  @AfterEach
  void tearDown() {
    crudUserDboRepository.deleteAll();
  }

  @Test
  void userRolesCorrectlyMapped() {
    UserDbo userDbo = new UserDbo(Username.create("some"));
    userDbo.setRoles(Set.of(RolesDbo.USER, RolesDbo.ADMIN));

    crudUserDboRepository.save(userDbo);

    String dbData = jdbcClient.sql("select roles from users").query(String.class).single();

    assertThat(dbData).contains("USER", "ADMIN");
  }
}
