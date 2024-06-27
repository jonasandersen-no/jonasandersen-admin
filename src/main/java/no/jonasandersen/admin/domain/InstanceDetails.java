package no.jonasandersen.admin.domain;

import java.util.ArrayList;
import java.util.List;

public record InstanceDetails(String region, String image, String label, String type, List<String> tags,
                              SensitiveString rootPassword, Boolean volume) {

  public static InstanceDetails createDefaultMinecraft(SensitiveString password) {
    return new InstanceDetails("se-sto", "linode/ubuntu22.04",
        "minecraft-auto-config-%d".formatted(System.currentTimeMillis()),
        "g6-dedicated-4", List.of("minecraft", "auto-created"), password, true);
  }

  public InstanceDetails withPrincipalTag(String owner) {
    List<String> tags = new ArrayList<>(tags());
    tags.add("owner:" + owner);

    return new InstanceDetails(
        region(),
        image(),
        label(),
        type(),
        List.copyOf(tags),
        rootPassword(),
        volume());
  }

  public InstanceDetails withServerType(ServerType serverType) {
    List<String> tags = new ArrayList<>(tags());
    tags.add("server-type:" + serverType.name());

    return new InstanceDetails(
        region(),
        image(),
        label(),
        type(),
        List.copyOf(tags),
        rootPassword(),
        volume());
  }

  public InstanceDetails withSubdomain(Subdomain subdomain) {
    if (subdomain == null) {
      return this;
    }

    List<String> tags = new ArrayList<>(tags());
    tags.add("subdomain:" + subdomain.value());

    return new InstanceDetails(
        region(),
        image(),
        label(),
        type(),
        List.copyOf(tags),
        rootPassword(),
        volume());
  }
}
