package no.jonasandersen.admin.domain;

import java.util.ArrayList;
import java.util.List;

public record InstanceDetails(
    String region,
    String image,
    String label,
    String type,
    List<String> tags,
    SensitiveString rootPassword,
    Boolean volume) {

  public static final String AUTO_CREATED_IMAGE_ID = "private/30572286";
  public static final String DEDICATED_8GB_RAM = "g6-dedicated-4";

  public static InstanceDetails createDefaultMinecraft(SensitiveString password) {
    return new InstanceDetails(
        "se-sto",
        AUTO_CREATED_IMAGE_ID,
        "minecraft-auto-config-%d".formatted(System.currentTimeMillis()),
        DEDICATED_8GB_RAM,
        List.of("minecraft", "auto-created"),
        password,
        true);
  }

  public InstanceDetails withPrincipalTag(String owner) {
    List<String> tags = new ArrayList<>(tags());
    tags.add("owner:" + owner);

    return new InstanceDetails(
        region(), image(), label(), type(), List.copyOf(tags), rootPassword(), volume());
  }

  public InstanceDetails withServerType(ServerType serverType) {
    List<String> tags = new ArrayList<>(tags());
    tags.add("server-type:" + serverType.name());

    return new InstanceDetails(
        region(), image(), label(), type(), List.copyOf(tags), rootPassword(), volume());
  }

  public InstanceDetails withSubdomain(Subdomain subdomain) {
    if (subdomain == null) {
      return this;
    }

    List<String> tags = new ArrayList<>(tags());
    tags.add("subdomain:" + subdomain.value());

    return new InstanceDetails(
        region(), image(), label(), type(), List.copyOf(tags), rootPassword(), volume());
  }
}
