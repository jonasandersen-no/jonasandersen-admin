package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.domain.LinodeInstance;
import org.junit.jupiter.api.Test;

class LinodeInstanceDatabaseRepositoryTest {

  @Test
  void newInstanceGetsIdWhenSaved() {
    var repository = LinodeInstanceDatabaseRepository.createNull();

    LinodeInstance saved = repository.save(LinodeInstance.createNull());

    assertThat(saved).isNotNull();
    assertThat(saved.id()).isEqualTo(0L);
  }

  @Test
  void existingInstanceIsNotStoredAsNew() {
    var repository = LinodeInstanceDatabaseRepository.createNull();

    LinodeInstance instance = LinodeInstance.createNull();
    LinodeInstance saved = repository.save(instance);

    assertThat(repository.save(saved)).isEqualTo(saved);
  }
}