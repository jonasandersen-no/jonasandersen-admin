package no.jonasandersen.admin.adapter.in.web.layout;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;

@ViewComponent
public class MainLayoutViewComponent {

  public record MainLayoutView(String title, ViewContext mainContent) implements ViewContext {

  }

  public MainLayoutView render(String title, ViewContext mainContent) {
    return new MainLayoutView(title, mainContent);
  }
}
