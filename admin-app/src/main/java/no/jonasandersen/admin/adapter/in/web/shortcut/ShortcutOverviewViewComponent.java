package no.jonasandersen.admin.adapter.in.web.shortcut;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;

@ViewComponent
public class ShortcutOverviewViewComponent {

  public record ShortcutOverviewView(List<String> projectNames) implements ViewContext {

  }

  public ShortcutOverviewView render(List<String> projectNames) {
    return new ShortcutOverviewView(projectNames);
  }
}
