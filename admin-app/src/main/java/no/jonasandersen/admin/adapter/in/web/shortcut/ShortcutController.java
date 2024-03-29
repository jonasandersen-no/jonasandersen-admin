package no.jonasandersen.admin.adapter.in.web.shortcut;

import no.jonasandersen.admin.core.shortcut.ShortcutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShortcutController {

  private final ShortcutService service;

  public ShortcutController(ShortcutService service) {
    this.service = service;
  }

  @GetMapping("/shortcut")
  String shortcut(Model model) {
    model.addAttribute("projectNames", service.getProjects());
    return "shortcut-overview";
  }

  @GetMapping("/shortcut/{project}")
  String shortcut(Model model, @PathVariable String project) {
    model.addAttribute("project", project);
    return "shortcut";
  }

}
