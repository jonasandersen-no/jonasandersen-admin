package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DatabaseAccessControlRepositoryTest extends IoBasedTest {

  @Autowired
  DatabaseAccessControlRepository repository;

  @Autowired
  CrudPermittedUserRepository db;

  @AfterEach
  void tearDown() {
    db.deleteAll();
  }

  @Test
  void addedUserIsFound() {
    User user = User.createUser("email@example.com");

    repository.addUser(user);

    assertThat(db.existsByEmail("email@example.com")).isTrue();
  }

  @Test
  void allUsersAreFound() {
    repository.addUser(User.createUser("email@example.com"));
    repository.addUser(User.createUser("email2@example.com"));

    List<User> users = repository.findAll();

    assertThat(users).hasSize(2);
  }

  @Test
  void findSingleUser() {
    repository.addUser(User.createUser("email@example.com"));

    User user = repository.findUser("email@example.com");

    assertThat(user).isNotNull();
  }

  @Test
  void removeUser() {
    PermittedUserDbo entity = new PermittedUserDbo();
    entity.setEmail("email@example.com");
    db.save(entity);

    repository.removeUser(User.createUser("email@example.com"));

    assertThat(db.findByEmail("email@example.com")).isNull();
  }
}