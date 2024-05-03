package no.jonasandersen.admin.adapter.out.linode.model.api.db;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "linode_instance", schema = "admin")
public record LinodeInstanceDbo(@Id Long id, String ips, String status, String label,
                                String tags, String volumeNames) {

  public LinodeInstance toDomain() {
    return new LinodeInstance(LinodeId.from(id), List.of(ips.split(",")), status, label,
        List.of(tags.split(",")), List.of(volumeNames.split(",")));
  }
}
