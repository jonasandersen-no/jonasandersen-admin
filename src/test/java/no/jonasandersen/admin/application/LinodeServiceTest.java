package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.domain.SensitiveString;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class LinodeServiceTest {

  @Test
  void principalNameIsAddedAsTagWhenCreatingInstance() {
    LinodeService service = LinodeService.createNull();

    LinodeInstance instance = service.createDefaultMinecraftInstance(SensitiveString.of("Password123!"));
    assertThat(instance.tags()).contains("owner:principalName");
  }

  @Test
  void ownerIsPopulatedWhenCreatingInstance() {
    LinodeService service = LinodeService.createNull();

    LinodeInstance instance = service.createDefaultMinecraftInstance(SensitiveString.of("Password123!"));
    assertThat(instance.owner()).isNotNull();
  }

  @Test
  void ownerExistsWhenFindingInstanceById() {

    LinodeService service = LinodeService.createNull();
    service.createDefaultMinecraftInstance(SensitiveString.of("Password123!"));

    Optional<LinodeInstance> found = service.findInstanceById(LinodeId.from(1L));

    assertThat(found).get().extracting(LinodeInstance::owner).isNotNull();
  }

  @Test
  void onlyAutoCreatedInstancesAreReturnedWhenGetInstancesIsCalled() {
    LinodeInstanceApi instance1 = Instancio.of(LinodeInstanceApi.class)
        .set(Select.field(LinodeInstanceApi::tags), List.of("auto-created"))
        .create();

    LinodeInstanceApi instance2 = Instancio.of(LinodeInstanceApi.class)
        .set(Select.field(LinodeInstanceApi::tags), List.of("not-auto-created"))
        .create();

    LinodeService service = LinodeService.createNull(List.of(instance1, instance2), List.of());

    assertThat(service.getInstances()).hasSize(1);
  }

  @Test
  void noInstanceWhenTagNotAutoCreated() {
    LinodeInstanceApi instance1 = Instancio.of(LinodeInstanceApi.class)
        .set(Select.field(LinodeInstanceApi::id), 1L)
        .set(Select.field(LinodeInstanceApi::tags), List.of("not-auto-created"))
        .create();

    LinodeService service = LinodeService.createNull(List.of(instance1), List.of());

    Optional<LinodeInstance> found = service.findInstanceById(LinodeId.from(1L));

    assertThat(found).isEmpty();
  }

  @Test
  void creatingInstancePublishesEvent() {
    LinodeService service = LinodeService.createNull();

    OutputTracker<Object> tracker = service.eventPublisher().track();

    service.createDefaultMinecraftInstance(SensitiveString.of("Password123!"));

    assertThat(tracker.data()).hasSize(1);
  }
}