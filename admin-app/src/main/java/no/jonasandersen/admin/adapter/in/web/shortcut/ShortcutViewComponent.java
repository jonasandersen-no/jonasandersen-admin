package no.jonasandersen.admin.adapter.in.web.shortcut;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;

@ViewComponent
public class ShortcutViewComponent {

  public record ShortcutView(String project) implements ViewContext {

  }

  public ViewContext render(String project) {
    return new ShortcutView(project);
  }
}
