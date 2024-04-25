package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.minecraft.MinecraftService;

@ViewComponent
public class LinodeDetailViewComponent {

  private final MinecraftService service;

  public LinodeDetailViewComponent(MinecraftService service) {
    this.service = service;
  }

  public record LinodeDetailView(LinodeInstance instance) implements ViewContext {

  }

  public LinodeDetailView render(LinodeId linodeId) {
    return new LinodeDetailView(service.getInstanceById(linodeId));
  }

}
