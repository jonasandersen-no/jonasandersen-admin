package no.jonasandersen.admin.core.minecraft;

import java.security.Principal;
import java.util.ArrayList;
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
import org.springframework.security.core.context.SecurityContextHolder;

public class LinodeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeService.class);
  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;
  private final Principal principal;

  public static LinodeService create(ServerApi serverApi, LinodeVolumeService linodeVolumeService) {
    return new LinodeService(serverApi, linodeVolumeService, new RealPrincipal());
  }

  public static LinodeService createNull() {
    return new LinodeService(LinodeServerApi.createNull(), null, new StubPrincipal());
  }

  private LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService, Principal principal) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
    this.principal = principal;
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
    return createInstance(InstanceDetails.createDefaultMinecraft("password"));
  }

  LinodeInstance createInstance(InstanceDetails instanceDetails) {
    return serverApi.createInstance(withPrincipalTag(instanceDetails));
  }

  InstanceDetails withPrincipalTag(InstanceDetails instanceDetails) {
    String name = principal.getName();

    List<String> tags = new ArrayList<>(instanceDetails.tags());
    tags.add(name);

    return new InstanceDetails(
        instanceDetails.region(),
        instanceDetails.image(),
        instanceDetails.label(),
        instanceDetails.type(),
        List.copyOf(tags),
        instanceDetails.rootPassword(),
        instanceDetails.volume());
  }

  // NULLABLES

  public static class RealPrincipal implements Principal {

    @Override
    public String getName() {
      return SecurityContextHolder.getContext().getAuthentication().getName();
    }
  }

  private static class StubPrincipal implements Principal {

    @Override
    public String getName() {
      return "principalName";
    }
  }
}
