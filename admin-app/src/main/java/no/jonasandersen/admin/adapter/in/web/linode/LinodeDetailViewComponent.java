package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.minecraft.LinodeService;

@ViewComponent
public class LinodeDetailViewComponent {

  private final LinodeService service;

  public LinodeDetailViewComponent(LinodeService service) {
    this.service = service;
  }

  public record LinodeDetailView(no.jonasandersen.admin.core.domain.LinodeInstance instance) implements ViewContext {

  }

  public LinodeDetailView render(LinodeId linodeId) {
    return new LinodeDetailView(service.getInstanceById(linodeId));
  }

}
