package no.jonasandersen.admin.core.shortcut;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import org.junit.jupiter.api.Test;

class ShortcutHtmlFormatterTest {

  @Test
  //language=HTML
  void formattedHtmlContainsShortcutInfo() {
    String formattedHtml = ShortcutHtmlFormatter.asTableRow(
        new Shortcut(1L, "Test", "Ctrl+Shift+T", "Open new tab"));

    assertThat(formattedHtml).isEqualTo("""
        <tr id="list-item-1">
          <td>Ctrl+Shift+T</td>
          <td>Open new tab</td>
          <td>
            <button
                hx-get="/hx/shortcut/edit/1"
                hx-target="#list-item-1"
                hx-swap="outerHTML"
                class="btn">
                Edit
              </button>
              <button
                hx-delete="/hx/shortcut/1"
                hx-target="#list-item-1"
                hx-swap="outerHTML"
                hx-confirm='Are you sure you want to delete this shortcut?'
                class="btn">
                Delete
              </button>
          </td>
        </tr>
        """);

  }
}