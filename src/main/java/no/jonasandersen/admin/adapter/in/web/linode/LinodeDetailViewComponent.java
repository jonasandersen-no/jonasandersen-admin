package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.domain.LinodeInstance;

@ViewComponent
public class LinodeDetailViewComponent {

  public record LinodeDetailView(LinodeInstance instance) implements ViewContext {

  }

  public LinodeDetailView render(LinodeInstance instance) {
    return new LinodeDetailView(instance);
  }

}
