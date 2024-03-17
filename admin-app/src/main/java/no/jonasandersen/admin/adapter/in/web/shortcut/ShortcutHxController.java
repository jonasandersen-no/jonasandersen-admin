package no.jonasandersen.admin.adapter.in.web.shortcut;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import java.util.List;
import no.jonasandersen.admin.ShortcutHtmlFormatter;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hx/shortcut")
public class ShortcutHxController {

  private final ShortcutService service;

  public ShortcutHxController(ShortcutService service) {
    this.service = service;
  }

  @PostMapping
  @HxRequest
  public String postShortcut(String project, String shortcut, String description) {
    Shortcut created = service.createShortcut(project, shortcut, description);

    List<Shortcut> shortcuts = service.getShortcuts();

    Shortcut shortcutFound = shortcuts.stream().filter(shortcut1 ->
        shortcut1.project().equalsIgnoreCase(project)
            && shortcut1.shortcut().equalsIgnoreCase(shortcut)
            && shortcut1.description().equalsIgnoreCase(description)).findFirst().orElseThrow();

    return ShortcutHtmlFormatter.tableRow(shortcutFound);
  }

  @PutMapping("/edit/{id}")
  @HxRequest
  public String editShortcut(@PathVariable Long id, String project, String shortcut,
      String description) {
    Shortcut shortcut1 = new Shortcut(id, project, shortcut, description);
    service.updateShortcut(shortcut1);

    List<Shortcut> shortcuts = service.getShortcuts();
    Shortcut shortcutFound = shortcuts.stream()
        .filter(shortcut2 -> shortcut2.id().equals(id))
        .findFirst().orElseThrow();

    return ShortcutHtmlFormatter.tableRow(shortcutFound);
  }

  @GetMapping("/edit/cancel/{id}")
  @HxRequest
  public String cancelEdit(@PathVariable Long id) {
    List<Shortcut> shortcuts = service.getShortcuts();
    Shortcut shortcut1 = shortcuts.stream()
        .filter(shortcut -> shortcut.id().equals(id))
        .findFirst().orElseThrow();

    return ShortcutHtmlFormatter.tableRow(shortcut1);
  }

  @GetMapping("/edit/{id}")
  @HxRequest
  public String editShortcut(@PathVariable Long id) {
    List<Shortcut> shortcuts = service.getShortcuts();
    Shortcut shortcut1 = shortcuts.stream()
        .filter(shortcut -> shortcut.id().equals(id))
        .findFirst().orElseThrow();

    return STR."""
        <tr id="list-item-\{shortcut1.id()}">
          <td>
            <input type="text" value="\{shortcut1.project()}" name="project" />
          </td>
          <td>
            <input type="text" value="\{shortcut1.shortcut()}" name="shortcut" />
          </td>
          <td>
            <input type="text" value="\{shortcut1.description()}" name="description" />
          </td>
          <td>
           <button
              hx-put="/hx/shortcut/edit/\{shortcut1.id()}"
              hx-target="#list-item-\{shortcut1.id()}"
              hx-swap="outerHTML"
              hx-include="closest tr"
              class="btn">
              Save
            </button>
            <button
              hx-get="/hx/shortcut/edit/cancel/\{shortcut1.id()}"
              hx-target="#list-item-\{shortcut1.id()}"
              hx-swap="outerHTML"
              class="btn">
              Cancel
            </button>
          </td>
        </tr>
        """;
  }

}
