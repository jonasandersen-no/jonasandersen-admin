package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import org.jetbrains.annotations.NotNull;

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

    return findVolumeForInstance(instance);
  }

  public List<no.jonasandersen.admin.core.domain.LinodeInstance> getInstances() {
    List<no.jonasandersen.admin.core.domain.LinodeInstance> instances = serverApi.getInstances();
    return instances.parallelStream()

        .map(this::findVolumeForInstance)
        .toList();
  }

  private @NotNull LinodeInstance findVolumeForInstance(LinodeInstance instance) {
    List<LinodeVolume> volumesByInstance = linodeVolumeService.getVolumesByInstance(
        instance.linodeId());

    List<String> volumeNames = volumesByInstance.stream()
        .map(LinodeVolume::label)
        .toList();

    return new LinodeInstance(instance.linodeId(), instance.ip(), instance.status(),
        instance.label(), instance.tags(), volumeNames, instance.specs());
  }

  public void createLinode(String instanceName, VolumeId volumeId) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public LinodeInstance createDefaultMinecraftInstance() {
    return serverApi.createInstance(InstanceDetails.createDefaultMinecraft("ThisIsAPassword123!"));
  }
}
