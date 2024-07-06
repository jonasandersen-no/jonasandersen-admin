package no.jonasandersen.admin.domain;

public record LinodeId(Long id) {

  public static LinodeId from(Long linodeId) {
    return new LinodeId(linodeId);
  }

  public static LinodeId createNull() {
    return new LinodeId(0L);
  }
}
