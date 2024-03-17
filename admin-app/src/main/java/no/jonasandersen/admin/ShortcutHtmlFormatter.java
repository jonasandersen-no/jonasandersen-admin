package no.jonasandersen.admin;

import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import org.jetbrains.annotations.NotNull;

public class ShortcutHtmlFormatter {

  public static @NotNull String tableRow(Shortcut shortcutFound) {
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
        </td>
        </tr>
        """;
  }
}
