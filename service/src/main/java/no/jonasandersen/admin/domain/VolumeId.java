package no.jonasandersen.admin.domain;

public record VolumeId(Long id) {

  public static VolumeId from(Long id) {
    return new VolumeId(id);
  }
}
