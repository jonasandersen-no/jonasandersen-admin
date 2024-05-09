package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;

public class LinodeService {

  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;

  public LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
  }

  public static LinodeService createNull() {
    return new LinodeService(LinodeServerApi.createNull(), null);
  }

  public no.jonasandersen.admin.core.domain.LinodeInstance getInstanceById(LinodeId linodeId) {
    no.jonasandersen.admin.core.domain.LinodeInstance instance = serverApi.getInstanceById(linodeId);

    List<LinodeVolume> volumesByInstance = linodeVolumeService.getVolumesByInstance(linodeId);

    List<String> volumeNames = volumesByInstance.stream()
        .map(LinodeVolume::label)
        .toList();

    return new no.jonasandersen.admin.core.domain.LinodeInstance(instance.linodeId(), instance.ip(), instance.status(),
        instance.label(), instance.tags(), volumeNames);
  }

  public List<no.jonasandersen.admin.core.domain.LinodeInstance> getInstances() {
    List<no.jonasandersen.admin.core.domain.LinodeInstance> instances = serverApi.getInstances();
    return instances.parallelStream()

        .map(instance -> {
          List<LinodeVolume> volumesByInstance = linodeVolumeService.getVolumesByInstance(
              instance.linodeId());

          List<String> volumeNames = volumesByInstance.stream()
              .map(LinodeVolume::label)
              .toList();

          return new no.jonasandersen.admin.core.domain.LinodeInstance(instance.linodeId(), instance.ip(), instance.status(),
              instance.label(), instance.tags(), volumeNames);
        })
        .toList();
  }

  public MinecraftInstance startMinecraftInstance(LinodeId linodeId) {
    no.jonasandersen.admin.core.domain.LinodeInstance instance = serverApi.getInstanceById(linodeId);

    if (instance != null) {
      return new MinecraftInstance(instance.label(), instance.ip().getFirst(), instance.status());
    }

    return null;
  }

  public void createLinode(String instanceName, VolumeId volumeId) {
//    serverApi.createLinode(instanceName, volumeId);
  }

  public LinodeInstance createDefaultMinecraftInstance() {
    return serverApi.createInstance(InstanceDetails.createDefaultMinecraft("ThisIsAPassword123!"));
  }
}
