package no.jonasandersen.admin.adapter.in.web.shortcut;

import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent;
import no.jonasandersen.admin.core.shortcut.ShortcutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShortcutController {

  private final MainLayoutViewComponent mainLayoutViewComponent;
  private final ShortcutOverviewViewComponent shortcutOverviewViewComponent;
  private final ShortcutViewComponent shortcutViewComponent;
  private final ShortcutService service;

  public ShortcutController(MainLayoutViewComponent mainLayoutViewComponent,
      ShortcutOverviewViewComponent shortcutOverviewViewComponent,
      ShortcutViewComponent shortcutViewComponent, ShortcutService service) {
    this.mainLayoutViewComponent = mainLayoutViewComponent;
    this.shortcutOverviewViewComponent = shortcutOverviewViewComponent;
    this.shortcutViewComponent = shortcutViewComponent;
    this.service = service;
  }

  @GetMapping("/shortcut")
  ViewContext shortcut() {
    return mainLayoutViewComponent.render("Shortcut",
        shortcutOverviewViewComponent.render(service.getProjects()));
  }

  @GetMapping("/shortcut/{project}")
  ViewContext shortcut(@PathVariable String project) {
    return mainLayoutViewComponent.render("Shortcut - " + project,
        shortcutViewComponent.render(project));
  }

}
