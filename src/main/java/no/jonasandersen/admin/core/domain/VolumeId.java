package no.jonasandersen.admin.core.domain;

public record VolumeId(Long id) {

  public static VolumeId from(Long id) {
    return new VolumeId(id);
  }
}
