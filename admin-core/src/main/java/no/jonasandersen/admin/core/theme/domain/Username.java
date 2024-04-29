package no.jonasandersen.admin.core.theme.domain;

public record Username(String value) {


  public static Username from(String name) {
    return new Username(name);
  }
}
