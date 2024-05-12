package no.jonasandersen.admin.application;

import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.domain.Ip;

public record ServerGeneratorResponse(Long linodeId, String label, Ip ip) {

  public static ServerGeneratorResponse from(LinodeInstance instance) {
    return new ServerGeneratorResponse(instance.linodeId().id(), instance.label(), new Ip(instance.ip().getFirst()));
  }
}
