package no.jonasandersen.admin.core.shortcut.domain;

public record Shortcut(Long id, String project, String shortcut, String description) {

  public static Shortcut from(Shortcut shortcut) {
    return new Shortcut(shortcut.id(), shortcut.project(), shortcut.shortcut(),
        shortcut.description());
  }
}
