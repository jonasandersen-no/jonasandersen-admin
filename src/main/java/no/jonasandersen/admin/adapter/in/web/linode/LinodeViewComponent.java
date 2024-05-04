package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;
import no.jonasandersen.admin.core.minecraft.LinodeService;

@ViewComponent
public class LinodeViewComponent {

  private final LinodeService service;
  private final LinodeCardViewComponent linodeCardViewComponent;

  public LinodeViewComponent(LinodeService service,
      LinodeCardViewComponent linodeCardViewComponent) {
    this.service = service;
    this.linodeCardViewComponent = linodeCardViewComponent;
  }

  public record LinodeView(List<no.jonasandersen.admin.core.domain.LinodeInstance> instances,
                           LinodeViewComponent linodeViewComponent) implements ViewContext {

  }

  public LinodeView render() {
    List<no.jonasandersen.admin.core.domain.LinodeInstance> instances = service.getInstances();
    return new LinodeView(instances, this);
  }

  public LinodeCardViewComponent.LinodeCardView renderCard(
      no.jonasandersen.admin.core.domain.LinodeInstance instance) {
    return linodeCardViewComponent.render(instance);
  }
}
