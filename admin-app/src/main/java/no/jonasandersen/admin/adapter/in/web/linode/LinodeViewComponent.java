package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import no.jonasandersen.admin.AdminProperties.Linode;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.MinecraftService;

@ViewComponent
public class LinodeViewComponent {

  private final MinecraftService service;
  private final LinodeCardViewComponent linodeCardViewComponent;

  public LinodeViewComponent(MinecraftService service,
      LinodeCardViewComponent linodeCardViewComponent) {
    this.service = service;
    this.linodeCardViewComponent = linodeCardViewComponent;
  }

  public record LinodeView(List<LinodeInstance> instances,
                           LinodeViewComponent linodeViewComponent) implements ViewContext {

  }

  public LinodeView render() {
    List<LinodeInstance> instances = service.getInstances();
    List<LinodeInstance> instances2 = service.getInstances();
    List<LinodeInstance> joined = new ArrayList<>();
    joined.addAll(instances);
    joined.addAll(instances2);
    return new LinodeView(joined, this);
  }

  public LinodeCardViewComponent.LinodeCardView renderCard(LinodeInstance instance) {
    return linodeCardViewComponent.render(instance);
  }
}
