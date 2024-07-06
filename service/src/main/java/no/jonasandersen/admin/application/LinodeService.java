package no.jonasandersen.admin.application;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.LinodeVolumeDto;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.InstanceNotFound;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeVolume;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.domain.Subdomain;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinodeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeService.class);
  public static final String AUTO_CREATED = "auto-created";
  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;

  public static LinodeService create(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    return new LinodeService(serverApi, linodeVolumeService);
  }

  public static LinodeService createNull() {
    return createNull(List.of(), List.of());
  }

  public static LinodeService createNull(List<LinodeInstanceApi> instances,
      List<LinodeVolumeDto> volumes) {
    return new LinodeService(LinodeServerApi.createNull(instances, volumes),
        LinodeVolumeService.createNull());
  }

  private LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
  }

  public Optional<LinodeInstance> findInstanceById(LinodeId linodeId) {
    Optional<LinodeInstance> instance = serverApi.findInstanceById(linodeId);

    if (instance.isEmpty()) {
      log.info("Instance not found: {}", linodeId);
      throw new InstanceNotFound("Instance not found");
    }

    if (!instance.get().tags().contains(AUTO_CREATED)) {
      log.info("Instance not auto-created: {}", linodeId);
      return Optional.empty();
    }

    return Optional.of(findVolumeForInstance(instance.get()));
  }

  public List<LinodeInstance> getInstances() {
    List<LinodeInstance> instances = serverApi.getInstances();
    List<LinodeInstance> filteredList = instances.stream()
        .filter(instance -> instance.tags().contains(AUTO_CREATED))
        .toList();

    return filteredList.parallelStream()
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

  LinodeInstance createDefaultMinecraftInstance(String owner, SensitiveString password, Subdomain subdomain) {
    return createInstance(owner, InstanceDetails.createDefaultMinecraft(password), subdomain, ServerType.MINECRAFT);
  }

  LinodeInstance createInstance(String owner, InstanceDetails instanceDetails,
      @NotNull Subdomain subdomain, ServerType serverType) {
    InstanceDetails withTags = instanceDetails
        .withPrincipalTag(owner)
        .withServerType(serverType)
        .withSubdomain(subdomain);

    return serverApi.createInstance(withTags);
  }
}
