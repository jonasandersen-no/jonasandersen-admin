package no.jonasandersen.admin.adapter.out.linode;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import no.jonasandersen.admin.domain.InstanceDetails;

public record CreateInstanceRequest(String region, String image, String label, String type, List<String> tags,
                                    @JsonProperty("root_pass") String rootPassword, Boolean volume) {

  public static CreateInstanceRequest from(InstanceDetails details) {
    return new CreateInstanceRequest(details.region(), details.image(), details.label(), details.type(),
        details.tags(), details.rootPassword().value(), details.volume());
  }
}
