package no.jonasandersen.admin.domain;

public record Theme(String value) {

  public static Theme from(String theme) {
    return new Theme(theme);
  }
}
