package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.domain.LinodeInstance;

@ViewComponent
public class LinodeCardViewComponent {

  public record LinodeCardView(LinodeInstance instance) implements ViewContext {

  }

  public LinodeCardView render(LinodeInstance instance) {
    return new LinodeCardView(instance);
  }
}
