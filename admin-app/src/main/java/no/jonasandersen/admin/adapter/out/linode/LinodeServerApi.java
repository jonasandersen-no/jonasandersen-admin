package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.domain.Ip;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
class LinodeServerApi implements ServerApi {

  private final LinodeBjoggisExchange linodeBjoggisExchange;
  private final LinodeExchange linodeExchange;
  private final Logger logger = LoggerFactory.getLogger(LinodeServerApi.class);

  LinodeServerApi(LinodeBjoggisExchange linodeBjoggisExchange, LinodeExchange linodeExchange) {
    this.linodeBjoggisExchange = linodeBjoggisExchange;
    this.linodeExchange = linodeExchange;
  }

  @Override
  public MinecraftInstance listServerInfo() {
    var result = linodeBjoggisExchange.listInstances();

    if (result.isEmpty()) {
      logger.info("No instances found");
      return new MinecraftInstance();
    }

    Instance first = result.getFirst();

    MinecraftInstance minecraftInstance = new MinecraftInstance();
    minecraftInstance.setName(first.label());
    minecraftInstance.setIp(new Ip(first.ip()));
    minecraftInstance.setStatus(first.status());
    return minecraftInstance;
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
}
