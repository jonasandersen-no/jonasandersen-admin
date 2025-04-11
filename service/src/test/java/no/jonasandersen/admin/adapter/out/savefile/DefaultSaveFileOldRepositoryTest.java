package no.jonasandersen.admin.adapter.out.savefile;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.application.port.UserRepository;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.SaveFileOld;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DefaultSaveFileOldRepositoryTest extends IoBasedTest {

  @Autowired
  private DefaultSaveFileRepository repository;

  @Autowired
  private JdbcSaveFileRepository db;

  @Autowired
  private UserRepository userRepository;

  @Test
  void canSaveFile() {

    userRepository.createNewUser(User.createUser("owner@email.com"));

    SaveFileOld saveFileOld = new SaveFileOld("saveFileOld", User.createUser("owner@email.com"));
    repository.save(saveFileOld);

    assertThat(db.count()).isEqualTo(1);
    assertThat(db.findAll().getFirst().getOwner()).isEqualTo(userRepository.getIdByEmail("owner@email.com"));

  }
}