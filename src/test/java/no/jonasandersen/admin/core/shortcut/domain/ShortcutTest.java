package no.jonasandersen.admin.core.shortcut.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ShortcutTest {

  @Test
  void fromMakesCopyOfProvidedShortcut() {
    Shortcut shortcut = new Shortcut(1L, "Test", "Ctrl+Shift+T", "Open new tab");
    Shortcut copied = Shortcut.from(shortcut);

    assertThat(copied).isEqualTo(shortcut);
  }

  @Test
  void assignIdMakesCopyOfProivdedShortcutButUpdatesId() {
    Shortcut shortcut = new Shortcut(1L, "Test", "Ctrl+Shift+T", "Open new tab");
    Shortcut copied = Shortcut.assignId(shortcut, 2L);

    assertThat(copied.id()).isEqualTo(2L);
    assertThat(copied.project()).isEqualTo(shortcut.project());
    assertThat(copied.shortcut()).isEqualTo(shortcut.shortcut());
    assertThat(copied.description()).isEqualTo(shortcut.description());
  }
}