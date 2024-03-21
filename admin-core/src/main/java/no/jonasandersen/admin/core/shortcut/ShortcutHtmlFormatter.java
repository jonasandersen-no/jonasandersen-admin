package no.jonasandersen.admin.core.shortcut;

import no.jonasandersen.admin.core.shortcut.domain.Shortcut;

public class ShortcutHtmlFormatter {

  private ShortcutHtmlFormatter() {
  }

  //language=HTML
  public static String asTableRow(Shortcut shortcutFound) {
    return STR."""
        <tr id="list-item-\{shortcutFound.id()}">
          <td>\{shortcutFound.project()}</td>
          <td>\{shortcutFound.shortcut()}</td>
          <td>\{shortcutFound.description()}</td>
          <td>
            <button
                hx-get="/hx/shortcut/edit/\{shortcutFound.id()}"
                hx-target="#list-item-\{shortcutFound.id()}"
                hx-swap="outerHTML"
                class="btn">
                Edit
              </button>
              <button
                hx-delete="/hx/shortcut/\{shortcutFound.id()}"
                hx-target="#list-item-\{shortcutFound.id()}"
                hx-swap="outerHTML"
                hx-confirm='Are you sure you want to delete this shortcut?'
                class="btn">
                Delete
              </button>
          </td>
        </tr>
        """;
  }
}
