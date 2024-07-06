package no.jonasandersen.admin.domain;


public record ServerGeneratorResponse(Long linodeId, String label, Ip ip) {

  public static ServerGeneratorResponse from(LinodeInstance instance) {
    return new ServerGeneratorResponse(instance.linodeId().id(), instance.label(), new Ip(instance.ip().getFirst()));
  }
}
