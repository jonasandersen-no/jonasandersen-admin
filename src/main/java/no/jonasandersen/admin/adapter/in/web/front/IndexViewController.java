package no.jonasandersen.admin.adapter.in.web.front;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;

@ViewComponent
public class IndexViewController {

  public record IndexView(String serverString) implements ViewContext {

  }

  public IndexView render() {
    return new IndexView("Hello from ViewComponents!");
  }
}
