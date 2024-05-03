package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import no.jonasandersen.admin.core.LinodeVolumeService;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class MinecraftService {

  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;

  public MinecraftService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
  }

  public MinecraftInstance findMinecraftInstance() {
    return serverApi.listServerInfo();
  }

  public LinodeInstance getInstanceById(LinodeId linodeId) {
    LinodeInstance instance = serverApi.getInstanceById(linodeId);


    List<LinodeVolume> volumesByInstance = linodeVolumeService.getVolumesByInstance(linodeId);

    List<String> volumeNames = volumesByInstance.stream()
        .map(LinodeVolume::label)
        .toList();

    return new LinodeInstance(instance.linodeId(), instance.ip(), instance.status(),
        instance.label(), instance.tags(), volumeNames);
  }

  public List<LinodeInstance> getInstances() {
    List<LinodeInstance> instances = serverApi.getInstances();
    return instances.parallelStream()

        .map(instance -> {
          List<LinodeVolume> volumesByInstance = linodeVolumeService.getVolumesByInstance(
              instance.linodeId());

          List<String> volumeNames = volumesByInstance.stream()
              .map(LinodeVolume::label)
              .toList();

          return new LinodeInstance(instance.linodeId(), instance.ip(), instance.status(),
              instance.label(), instance.tags(), volumeNames);
        })
        .toList();
  }

  public void startMinecraftInstance() {

  }

  public void createLinode(String instanceName, VolumeId volumeId) {
//    serverApi.createLinode(instanceName, volumeId);
  }
}
