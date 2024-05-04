package no.jonasandersen.admin.adapter.out.linode.model.api.db;

import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "linode_volume", schema = "admin")
public record LinodeVolumeDbo(@Id Long id, String label, String status,
                              Long linodeId) {

  public LinodeVolume toDomain() {
    return new LinodeVolume(VolumeId.from(id), label, status, LinodeId.from(linodeId));
  }
}