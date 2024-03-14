package no.jonasandersen.admin.adapter.in.web.shortcut;

import no.jonasandersen.admin.core.shortcut.ShortcutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShortcutController {

  private final ShortcutService service;

  public ShortcutController(ShortcutService service) {
    this.service = service;
  }

  @GetMapping("/shortcut")
  String shortcut(Model model) {
    model.addAttribute("shortcuts", service.getShortcuts());
    return "shortcut";
  }

}
