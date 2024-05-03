package no.jonasandersen.admin.adapter.in.web.linode.create;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeVolume;

@ViewComponent
public class CreateFormComponent {

  public record VolumeIdAndString(Long id, String name) {

  }

  public record CreateFromView(List<VolumeIdAndString> volumes) implements ViewContext {

  }

  public ViewContext render(List<LinodeVolume> volumes) {
    List<VolumeIdAndString> values = volumes.stream()
        .map(volume -> new VolumeIdAndString(volume.id().id(), volume.label()))
        .toList();

    return new CreateFromView(values);
  }
}
