package no.jonasandersen.admin.core.shortcut;

import no.jonasandersen.admin.core.shortcut.domain.Shortcut;

public class ShortcutHtmlFormatter {

  private ShortcutHtmlFormatter() {
  }

  //language=HTML
  public static String asTableRow(Shortcut shortcutFound) {
    return """
        <tr id="list-item-%s">
          <td>%s</td>
          <td>%s</td>
          <td>
            <button
                hx-get="/hx/shortcut/edit/%s"
                hx-target="#list-item-%s"
                hx-swap="outerHTML"
                class="btn">
                Edit
              </button>
              <button
                hx-delete="/hx/shortcut/%s"
                hx-target="#list-item-%s"
                hx-swap="outerHTML"
                hx-confirm='Are you sure you want to delete this shortcut?'
                class="btn">
                Delete
              </button>
          </td>
        </tr>
        """.formatted(
        shortcutFound.id(),
        shortcutFound.shortcut(),
        shortcutFound.description(),
        shortcutFound.id(),
        shortcutFound.id(),
        shortcutFound.id(),
        shortcutFound.id()
    );
  }
}
