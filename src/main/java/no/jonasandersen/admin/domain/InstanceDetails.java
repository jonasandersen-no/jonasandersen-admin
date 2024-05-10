package no.jonasandersen.admin.domain;

import java.util.List;

public record InstanceDetails(String region, String image, String label, String type, List<String> tags,
                              SensitiveString rootPassword, Boolean volume) {

  public static InstanceDetails createDefaultMinecraft(String password) {
    return new InstanceDetails("se-sto", "linode/ubuntu22.04",
        "minecraft-auto-config-%d".formatted(System.currentTimeMillis()),
        "g6-dedicated-4", List.of("minecraft", "auto-created"), SensitiveString.of(password), true);
  }
}
