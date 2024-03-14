package no.jonasandersen.admin.core.shortcut.domain;


import static org.assertj.core.api.Assertions.assertThat;

import fixture.TestShortcutRepository;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import org.junit.jupiter.api.Test;

class ShortcutServiceTest {

  @Test
  void shortcutCreatedIsCorrect() {
    ShortcutService service = new ShortcutService(new TestShortcutRepository());
    Shortcut shortcut = service.createShortcut("Test", "Ctrl+Shift+T",
        "Open new tab");

    assertThat(shortcut.project()).isEqualTo("Test");
    assertThat(shortcut.shortcut()).isEqualTo("Ctrl+Shift+T");
    assertThat(shortcut.description()).isEqualTo("Open new tab");
  }

  @Test
  void shortcutCreatedAddsOneToListOfShortcuts() {
    ShortcutService service = new ShortcutService(new TestShortcutRepository());
    service.createShortcut("Test", "Ctrl+Shift+T",
        "Open new tab");

    assertThat(service.getShortcuts()).hasSize(1);
  }

  @Test
  void listShortcutsContainsCreatedShortcut() {
    ShortcutService service = new ShortcutService(new TestShortcutRepository());
    Shortcut shortcut = service.createShortcut("Test", "Ctrl+Shift+T",
        "Open new tab");

    assertThat(service.getShortcuts()).contains(shortcut);
  }
}