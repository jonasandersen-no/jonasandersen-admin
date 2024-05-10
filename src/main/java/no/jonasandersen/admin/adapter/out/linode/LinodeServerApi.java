package no.jonasandersen.admin.adapter.out.linode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Alerts;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Backups;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Schedule;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Specs;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinodeServerApi implements ServerApi {

  private final LinodeExchange linodeExchange;
  private final Logger logger = LoggerFactory.getLogger(LinodeServerApi.class);


  public static LinodeServerApi create(LinodeExchange linodeExchange) {
    return new LinodeServerApi(linodeExchange);
  }

  public static LinodeServerApi createNull() {
    return new LinodeServerApi(new StubLinodeExchange());
  }

  private LinodeServerApi(LinodeExchange linodeExchange) {
    this.linodeExchange = linodeExchange;
  }

  @Override
  public LinodeInstance createInstance(InstanceDetails instanceDetails) {
    logger.info("Creating instance with details: {}", instanceDetails);

    LinodeInstanceApi linodeInstanceApi = linodeExchange.createInstance(instanceDetails);
    return linodeInstanceApi.toDomain();
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    Page<LinodeVolumeDto> volumes = linodeExchange.volumes();

    return volumes.data().stream().map(
            volume -> {
              VolumeId volumeId = new VolumeId(volume.id());
              LinodeId linodeId =
                  volume.linodeId() == null ? null : new LinodeId(volume.linodeId());

              return new LinodeVolume(volumeId, volume.label(), volume.status(), linodeId);
            })
        .toList();
  }

  @Override
  public List<LinodeInstance> getInstances() {
    Page<LinodeInstanceApi> list = linodeExchange.list();

    List<LinodeInstanceApi> data = list.data();

    return data.stream()
        .map(LinodeInstanceApi::toDomain)
        .toList();
  }

  @Override
  public LinodeInstance getInstanceById(LinodeId linodeId) {
    LinodeInstanceApi linodeInstanceApi = linodeExchange.getInstanceById(linodeId.id());

    return linodeInstanceApi.toDomain();
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    Page<LinodeVolumeDto> volumes = linodeExchange.volumes(linodeId.id().toString());

    return volumes.data().stream()
        .map(
            volume -> {
              VolumeId volumeId = new VolumeId(volume.id());
              LinodeId linodeId1 = new LinodeId(volume.linodeId());
              return new LinodeVolume(volumeId, volume.label(), volume.status(), linodeId1);
            })
        .toList();
  }


  private static class StubLinodeExchange implements LinodeExchange {

    private final List<LinodeInstanceApi> instances = new ArrayList<>();
    private Long linodeId = 1L;

    @Override
    public Page<LinodeInstanceApi> list() {
      return new Page<>(List.copyOf(instances), 0, 1, instances.size());
    }

    @Override
    public LinodeInstanceApi getInstanceById(Long linodeId) {
      return instances.stream()
          .filter(instance -> linodeId.equals(instance.id()))
          .findFirst()
          .orElseThrow();
    }

    @Override
    public Page<LinodeVolumeDto> volumes(String linodeId) {
      return null;
    }

    @Override
    public Page<LinodeVolumeDto> volumes() {
      return null;
    }

    @Override
    public LinodeInstanceApi createInstance(InstanceDetails instanceDetails) {
      LinodeInstanceApi instance = new LinodeInstanceApi(linodeId++,
          instanceDetails.label(),
          "group",
          "running",
          LocalDateTime.now(),
          LocalDateTime.now(),
          instanceDetails.type(),
          List.of("127.0.0.1"),
          "::1",
          instanceDetails.image(),
          instanceDetails.region(),
          new Specs(81920, 4096, 2, 0, 4000),
          new Alerts(100, 10, 10, 80, 10000),
          new Backups(true, true, new Schedule("Saturday", "W22"), LocalDateTime.now()),
          "kvm",
          false,
          List.copyOf(instanceDetails.tags()),
          "21e5aaacb4064de1951324ce58a2c41b",
          false);
      instances.add(instance);
      return instance;
    }
  }
}
