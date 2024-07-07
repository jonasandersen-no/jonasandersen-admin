package no.jonasandersen.admin.domain;


import java.util.List;

public record ServerGeneratorResponse(Long linodeId, String label, Ip ip, java.util.List<String> tags) {

  public static ServerGeneratorResponse from(LinodeInstance instance) {
    return new ServerGeneratorResponse(instance.linodeId().id(), instance.label(), new Ip(instance.ip().getFirst()),
        List.copyOf(instance.tags()));
  }

  public String owner() {
    return tags.stream()
        .filter(tag -> tag.startsWith("owner:"))
        .map(tag -> tag.substring(6))
        .findFirst()
        .orElse("unknown");
  }
}
