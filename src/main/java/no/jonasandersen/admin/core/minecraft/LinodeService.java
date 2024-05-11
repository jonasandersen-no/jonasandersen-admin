package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.domain.InstanceNotFound;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinodeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeService.class);
  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;

  public LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
  }

  public static LinodeService createNull() {
    return new LinodeService(LinodeServerApi.createNull(), null);
  }

  public LinodeInstance getInstanceById(LinodeId linodeId) {
    Optional<LinodeInstance> instance = serverApi.findInstanceById(linodeId);

    if (instance.isEmpty()) {
      log.info("Instance not found: {}", linodeId);
      throw new InstanceNotFound("Instance not found");
    }

    return findVolumeForInstance(instance.get());
  }


  public List<LinodeInstance> getInstances() {
    List<LinodeInstance> instances = serverApi.getInstances();
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
