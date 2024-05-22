package no.jonasandersen.admin.application;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.DefaultEventPublisher;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.LinodeVolumeDto;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.port.EventPublisher;
import no.jonasandersen.admin.core.domain.InstanceNotFound;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.LinodeVolumeService;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SaveLinodeInstanceEvent;
import no.jonasandersen.admin.domain.SensitiveString;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class LinodeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeService.class);
  public static final String AUTO_CREATED = "auto-created";
  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;
  private final Principal principal;
  private final EventPublisher eventPublisher;

  public static LinodeService create(ServerApi serverApi, LinodeVolumeService linodeVolumeService,
      EventPublisher eventPublisher) {
    return new LinodeService(serverApi, linodeVolumeService, new RealPrincipal(), eventPublisher);
  }

  public static LinodeService createNull() {
    return createNull(List.of(), List.of());
  }

  public static LinodeService createNull(List<LinodeInstanceApi> instances, List<LinodeVolumeDto> volumes) {
    return new LinodeService(LinodeServerApi.createNull(instances, volumes), LinodeVolumeService.createNull(),
        new StubPrincipal(), DefaultEventPublisher.createNull());
  }

  private LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService, Principal principal,
      EventPublisher eventPublisher) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
    this.principal = principal;
    this.eventPublisher = eventPublisher;
  }

  @Deprecated
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

    return Optional.of(findVolumeForInstance(instance.get()).withOwner(principal.getName()));
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

    return new LinodeInstance(null, instance.linodeId(), instance.ip(), null, instance.status(),
        instance.label(), instance.tags(), volumeNames, instance.specs());
  }

  LinodeInstance createDefaultMinecraftInstance(SensitiveString password) {
    return createInstance(InstanceDetails.createDefaultMinecraft(password));
  }

  private LinodeInstance createInstance(InstanceDetails instanceDetails) {
    LinodeInstance instance = serverApi.createInstance(withPrincipalTag(instanceDetails));
    SaveLinodeInstanceEvent event = new SaveLinodeInstanceEvent(null, instance.linodeId(),
        principal.getName(), null, null);
    eventPublisher.publishEvent(event);

    return instance.withOwner(principal.getName());
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

  public EventPublisher eventPublisher() {
    return eventPublisher;
  }

  // NULLABLES

  public static class RealPrincipal implements Principal {

    @Override
    public String getName() {
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
        return SecurityContextHolder.getContext().getAuthentication().getName();
      }
      return "unknown";
    }
  }

  private static class StubPrincipal implements Principal {

    @Override
    public String getName() {
      return "principalName";
    }
  }
}
