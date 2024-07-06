package no.jonasandersen.admin.adapter.out.linode.api.model;

import java.time.LocalDateTime;
import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Alerts;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Backups;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Specs;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeSpecs;

public record LinodeInstanceApi(
    long id,
    String label,
    String group,
    String status,
    LocalDateTime created,
    LocalDateTime updated,
    String type,
    List<String> ipv4,
    String ipv6,
    String image,
    String region,
    Specs specs,
    Alerts alerts,
    Backups backups,
    String hypervisor,
    boolean watchdogEnabled,
    List<String> tags,
    String hostUuid,
    boolean hasUserData
) {

  public LinodeInstanceApi {
    if (id <= 0) {
      throw new IllegalArgumentException("Id must be positive");
    }
    if (ipv4 == null || ipv4.isEmpty()) {
      throw new IllegalArgumentException("At least one IPv4 address is required");
    }
    if (ipv6 == null || ipv6.isEmpty()) {
      throw new IllegalArgumentException("IPv6 address is required");
    }
  }

  public LinodeInstance toDomain() {
    return new LinodeInstance(new LinodeId(id), List.copyOf(ipv4), status, label, tags, List.of(),
        new LinodeSpecs(specs.memory(), specs.vcpus()));
  }
}
