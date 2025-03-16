package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import no.jonasandersen.admin.adapter.out.savefile.JdbcSaveFileRepository;
import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.Roles;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DefaultUserRepositoryTest extends IoBasedTest {

  @Autowired
  private CrudUserDboRepository crudRepository;

  @Autowired
  private JdbcSaveFileRepository crudSaveFileRepository;

  @Autowired
  private UserRepository repository;

  @BeforeEach
  @AfterEach
  void setUp() {
    // Find a way to not rely on deletion in order to make the test pass

    crudSaveFileRepository.deleteAll();
    crudRepository.deleteAll();
  }

  @Test
  void userExistsByEmail() {
    crudRepository.save(new UserDbo(Username.create("test@example.com")));

    assertThat(repository.existsByEmail("test@example.com")).isTrue();
  }

  @Test
  void userFoundByEmail() {
    crudRepository.save(new UserDbo(Username.create("test@example.com"), Set.of(RolesDbo.USER)));

    User user = repository.findByEmail("test@example.com");

    assertThat(user).isNotNull();
    assertThat(user.username().value()).isEqualTo("test@example.com");
    assertThat(user.roles()).contains(Roles.USER);
  }

  @Test
  void createdUserExistsInDb() {

    repository.createNewUser(new User(Username.create("savedUser"), Set.of(Roles.ADMIN)));

    assertThat(crudRepository.count()).isEqualTo(1);
  }
}