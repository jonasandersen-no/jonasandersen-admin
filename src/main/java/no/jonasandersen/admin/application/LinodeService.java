package no.jonasandersen.admin.application;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.adapter.out.linode.LinodeVolumeDto;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.InstanceNotFound;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeVolume;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.domain.ServerType;
import no.jonasandersen.admin.infrastructure.Features;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinodeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeService.class);
  public static final String AUTO_CREATED = "auto-created";
  private final ServerApi serverApi;
  private final LinodeVolumeService linodeVolumeService;
  private final DnsApi dnsApi;

  public static LinodeService create(ServerApi serverApi, LinodeVolumeService linodeVolumeService, DnsApi dnsApi) {
    return new LinodeService(serverApi, linodeVolumeService, dnsApi);
  }

  public static LinodeService createNull() {
    return createNull(List.of(), List.of());
  }

  public static LinodeService createNull(List<LinodeInstanceApi> instances,
      List<LinodeVolumeDto> volumes) {
    return new LinodeService(LinodeServerApi.createNull(instances, volumes),
        LinodeVolumeService.createNull(), new StubDnsApi());
  }

  private LinodeService(ServerApi serverApi, LinodeVolumeService linodeVolumeService,
      DnsApi dnsApi) {
    this.serverApi = serverApi;
    this.linodeVolumeService = linodeVolumeService;
    this.dnsApi = dnsApi;
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

    return new LinodeInstance(null, instance.linodeId(), instance.ip(), instance.status(),
        instance.label(), instance.tags(), volumeNames, instance.specs());
  }

  LinodeInstance createDefaultMinecraftInstance(String owner, SensitiveString password, String subdomain) {
    return createInstance(owner, InstanceDetails.createDefaultMinecraft(password), subdomain, ServerType.MINECRAFT);
  }

  private LinodeInstance createInstance(String owner, InstanceDetails instanceDetails,
      String subdomain, ServerType serverType) {
    InstanceDetails withTags = instanceDetails.withPrincipalTag(owner).withServerType(serverType);
    LinodeInstance instance = serverApi.createInstance(withTags);

    if (!Features.isEnabled(Feature.LINODE_STUB)) {
      if (subdomain != null && !subdomain.isBlank()) {
        dnsApi.overwriteDnsRecord(new Ip(instance.ip().getFirst()), owner, subdomain);
      }
    }

    return instance;
  }
}
