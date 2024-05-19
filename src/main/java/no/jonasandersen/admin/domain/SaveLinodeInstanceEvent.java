package no.jonasandersen.admin.domain;

import no.jonasandersen.admin.core.domain.LinodeId;

public record SaveLinodeInstanceEvent(Long id, LinodeId linodeId, String createdBy, String serverType,
                                      String subDomain) {

  public static SaveLinodeInstanceEvent createNull() {
    return new SaveLinodeInstanceEvent(null, LinodeId.createNull(), "", "", "");
  }
}
