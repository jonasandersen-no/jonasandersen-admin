package no.jonasandersen.admin.core.theme.domain;

public record Theme(String value) {

  public static Theme from(String theme) {
    return new Theme(theme);
  }
}
