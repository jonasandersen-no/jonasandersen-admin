package no.jonasandersen.admin.adapter.out.linode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SensitiveString;
import org.junit.jupiter.api.Test;

class LinodeServerApiTest {

  @Test
  void instanceCreatedWhenDetailsIsProvided() {
    LinodeServerApi serverApi = LinodeServerApi.createNull();

    LinodeInstance instance = serverApi.createInstance(InstanceDetails.createDefaultMinecraft(
        SensitiveString.of("password")));

    assertThat(instance).isNotNull();
    assertThat(instance.linodeId()).isEqualTo(LinodeId.from(1L));
    assertThat(instance.label()).startsWith("minecraft-auto-config-");

  }

  @Test
  void instanceIsReturnedWhenGettingById() {
    LinodeServerApi serverApi = LinodeServerApi.createNull();

    LinodeInstance instance = serverApi.createInstance(InstanceDetails.createDefaultMinecraft(
        SensitiveString.of("password")));

    Optional<LinodeInstance> instanceById = serverApi.findInstanceById(instance.linodeId());

    assertThat(instanceById.isPresent()).isTrue();
    assertThat(instanceById.get().linodeId()).isEqualTo(instance.linodeId());
  }

  @Test
  void allInstancesIsReturnedWhenGettingInstances() {
    LinodeServerApi serverApi = LinodeServerApi.createNull();

    serverApi.createInstance(InstanceDetails.createDefaultMinecraft(SensitiveString.of("password")));
    serverApi.createInstance(InstanceDetails.createDefaultMinecraft(SensitiveString.of("password1")));

    List<LinodeInstance> instances = serverApi.getInstances();

    assertThat(instances).hasSize(2);
  }

  @Test
  void allVolumesIsReturnedWhenGettingVolumes() {
    LinodeVolumeDto volumeDto = new LinodeVolumeDto(1L, "volume1", "active", 2L);
    LinodeVolumeDto volumeDto1 = new LinodeVolumeDto(2L, "volume2", "active", 3L);

    LinodeServerApi serverApi = LinodeServerApi.createNull(Collections.emptyList(), List.of(volumeDto, volumeDto1));

    List<LinodeVolume> volumes = serverApi.getVolumes();

    assertThat(volumes).hasSize(2);
    assertThat(volumes.get(0).id()).isEqualTo(new VolumeId(1L));
    assertThat(volumes.get(1).id()).isEqualTo(new VolumeId(2L));
  }

  @Test
  void volumeIsReturnedWhenLinodeIdIsPassed() {
    LinodeVolumeDto volumeDto = new LinodeVolumeDto(1L, "volume1", "active", 2L);

    LinodeServerApi serverApi = LinodeServerApi.createNull(Collections.emptyList(), List.of(volumeDto));

    List<LinodeVolume> volumes = serverApi.getVolumesByInstance(LinodeId.from(2L));
    assertThat(volumes).hasSize(1);
    assertThat(volumes.getFirst().id()).isEqualTo(new VolumeId(1L));
  }

}