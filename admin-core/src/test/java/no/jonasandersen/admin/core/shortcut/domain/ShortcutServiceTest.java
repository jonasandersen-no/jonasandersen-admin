package no.jonasandersen.admin.core.shortcut.domain;


import static org.assertj.core.api.Assertions.assertThat;

import fixture.TestShortcutRepository;
import java.util.List;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShortcutServiceTest {

  @Nested
  class Create {

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
  }

  @Nested
  class Find {

    @Test
    void findShortcutsByProjectReturnsCorrectShortcuts() {
      ShortcutService service = new ShortcutService(new TestShortcutRepository());
      service.createShortcut("Test", "Ctrl+Shift+T",
          "Open new tab");
      service.createShortcut("Test2", "Ctrl+Shift+N",
          "Open new window");
      service.createShortcut("Test3", "Ctrl+Shift+W",
          "Close window");

      assertThat(service.getShortcutsByProject("Test")).hasSize(1);
    }

    @Test
    void findShortcutsReturnsAllShortcuts() {
      ShortcutService service = new ShortcutService(new TestShortcutRepository());
      service.createShortcut("Test", "Ctrl+Shift+T",
          "Open new tab");
      service.createShortcut("Test2", "Ctrl+Shift+N",
          "Open new window");
      service.createShortcut("Test3", "Ctrl+Shift+W",
          "Close window");

      assertThat(service.getShortcuts()).hasSize(3);
    }
  }

  @Nested
  class Update {

    @Test
    void updateShortcutReturnsCorrectShortcut() {
      ShortcutService service = new ShortcutService(new TestShortcutRepository());
      Shortcut shortcut = service.createShortcut("UPDATE-TEST", "Ctrl+Shift+T",
          "Open new tab");

      service.updateShortcut(new Shortcut(shortcut.id(), "UPDATE-TEST", "Ctrl+Shift+L",
          "Search"));

      List<Shortcut> shortcuts = service.getShortcuts();
      Shortcut updated = shortcuts.stream()
          .filter(shortcut1 -> shortcut1.id().equals(shortcut.id()))
          .findFirst().orElseThrow();

      assertThat(updated.project()).isEqualTo("UPDATE-TEST");
      assertThat(updated.shortcut()).isEqualTo("Ctrl+Shift+L");
      assertThat(updated.description()).isEqualTo("Search");
    }
  }
}