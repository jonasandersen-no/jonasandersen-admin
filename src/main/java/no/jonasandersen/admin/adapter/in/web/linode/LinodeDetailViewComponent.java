package no.jonasandersen.admin.adapter.in.web.linode;

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent;
import de.tschuehly.spring.viewcomponent.jte.ViewContext;
import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeSpecs;

@ViewComponent
public class LinodeDetailViewComponent {

  public record LinodeDetailView(Long id, LinodeId linodeId, List<String> ip, String owner, String status, String label,
                                 List<String> tags, List<String> volumeNames, LinodeSpecs specs) implements
      ViewContext {

    public String prettyPrintTags() {
      if (tags.isEmpty()) {
        return "-";
      }
      return String.join(", ", tags);
    }

    public String prettyPrintVolumeNames() {
      if (volumeNames.isEmpty()) {
        return "-";
      }
      return String.join(", ", volumeNames);
    }

    public String prettyPrintLabel() {
      return label.substring(0, 1).toUpperCase() + label.substring(1).toLowerCase();
    }

    public String prettyPrintStatus() {
      return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }

    public String prettyPrintIp() {
      return String.join(", ", ip);
    }

    public String prettyPrintSpecs() {
      return specs.memory() + " MB";
    }
  }

  public LinodeDetailView render(LinodeInstance instance) {
    return new LinodeDetailView(instance.id(), instance.linodeId(), instance.ip(), instance.owner(), instance.status(),
        instance.label(), instance.tags(), instance.volumeNames(), instance.specs());
  }

}
