package no.jonasandersen.admin.adapter.in.web.linode.create;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;
import no.jonasandersen.admin.application.ServerGenerator.ServerType;

@ViewComponent
public class CreateFormComponent {

  public record CreateFromView(List<ServerType> serverTypes) implements ViewContext {

  }

  public ViewContext render() {
    return new CreateFromView(List.of(ServerType.values()));
  }
}
