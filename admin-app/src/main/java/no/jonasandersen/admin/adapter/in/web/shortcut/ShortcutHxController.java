package no.jonasandersen.admin.adapter.in.web.shortcut;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import java.util.List;
import java.util.stream.Collectors;
import no.jonasandersen.admin.core.shortcut.ShortcutHtmlFormatter;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @GetMapping("/{project}")
  @HxRequest
  String getShortcuts(@PathVariable String project) {
    List<Shortcut> shortcuts = service.getShortcutsByProject(project);
    return shortcuts.stream()
        .map(ShortcutHtmlFormatter::asTableRow)
        .collect(Collectors.joining());
  }

  @GetMapping("/all")
  @HxRequest
  String getAllShortcuts() {
    String collect = service.getShortcuts().stream()
        .map(ShortcutHtmlFormatter::asTableRow)
        .collect(Collectors.joining());
    return collect;
  }

  @PostMapping
  @HxRequest
  public void postShortcut(String project, String shortcut, String description) {
    Shortcut created = service.createShortcut(project, shortcut, description);

    List<Shortcut> shortcuts = service.getShortcuts();

    Shortcut shortcutFound = shortcuts.stream().filter(shortcut1 ->
        shortcut1.project().equalsIgnoreCase(project)
            && shortcut1.shortcut().equalsIgnoreCase(shortcut)
            && shortcut1.description().equalsIgnoreCase(description)).findFirst().orElseThrow();

//    return ShortcutHtmlFormatter.asTableRow(shortcutFound);
  }

  @DeleteMapping("/{id}")
  @HxRequest
  String deleteShortcut(@PathVariable Long id) {
    service.deleteShortcut(id);
    return "";
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

    return ShortcutHtmlFormatter.asTableRow(shortcutFound);
  }

  @GetMapping("/edit/cancel/{id}")
  @HxRequest
  public String cancelEdit(@PathVariable Long id) {
    List<Shortcut> shortcuts = service.getShortcuts();
    Shortcut shortcut1 = shortcuts.stream()
        .filter(shortcut -> shortcut.id().equals(id))
        .findFirst().orElseThrow();

    return ShortcutHtmlFormatter.asTableRow(shortcut1);
  }

  @GetMapping("/projects")
  @HxRequest
  String getProjects() {
    List<String> projects = service.getProjects();

    String options = projects.stream()
        .map(project -> """
            <option value="%s">%s</option>
            """.formatted(project, project))
        .collect(Collectors.joining());

    return """
           <select name="project" id="project-select" hx-get="/hx/shortcut" hx-swap="innerHTML" hx-trigger="change, load" hx-target="#table-body">
           %s
           </select>
        """.formatted(options);

  }

  @GetMapping("/edit/{id}")
  @HxRequest
  public String editShortcut(@PathVariable Long id) {
    List<Shortcut> shortcuts = service.getShortcuts();
    Shortcut shortcut1 = shortcuts.stream()
        .filter(shortcut -> shortcut.id().equals(id))
        .findFirst().orElseThrow();

    return """
        <tr id="list-item-%s">
          <td>
            <input type="text" value="%s" name="project" />
          </td>
          <td>
            <input type="text" value="%s" name="shortcut" />
          </td>
          <td>
            <input type="text" value="%s" name="description" />
          </td>
          <td>
           <button
              hx-put="/hx/shortcut/edit/%s"
              hx-target="#list-item-%s"
              hx-swap="outerHTML"
              hx-include="closest tr"
              class="btn">
              Save
            </button>
            <button
              hx-get="/hx/shortcut/edit/cancel/%s"
              hx-target="#list-item-%s"
              hx-swap="outerHTML"
              class="btn">
              Cancel
            </button>
          </td>
        </tr>
        """.formatted(shortcut1.id(), shortcut1.project(), shortcut1.shortcut(),
        shortcut1.description(), shortcut1.id(), shortcut1.id(), shortcut1.id(), shortcut1.id());
  }

}
