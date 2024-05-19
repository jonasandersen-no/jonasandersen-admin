package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeStoredInfo;
import no.jonasandersen.admin.domain.SaveLinodeInstanceEvent;
import org.junit.jupiter.api.Test;

class LinodeInstanceDatabaseRepositoryTest {

  @Test
  void newInstanceGetsIdWhenSaved() {
    var repository = LinodeInstanceDatabaseRepository.createNull();

    repository.save(SaveLinodeInstanceEvent.createNull());

    List<LinodeStoredInfo> instances = repository.getInstances();
    assertThat(instances).hasSize(1);
    assertThat(instances.getFirst().id()).isEqualTo(0L);
  }

  @Test
  void existingInstanceIsNotStoredAsNew() {
    var repository = LinodeInstanceDatabaseRepository.createNull();

    repository.save(SaveLinodeInstanceEvent.createNull());

    List<LinodeStoredInfo> instances = repository.getInstances();
    LinodeStoredInfo first = instances.getFirst();
    SaveLinodeInstanceEvent event = new SaveLinodeInstanceEvent(first.id(), LinodeId.from(first.linodeId()),
        first.createdBy(), first.serverType(),
        first.subDomain());

    repository.onSaveEvent(event);

    assertThat(repository.getInstances()).isEqualTo(instances);
  }
}