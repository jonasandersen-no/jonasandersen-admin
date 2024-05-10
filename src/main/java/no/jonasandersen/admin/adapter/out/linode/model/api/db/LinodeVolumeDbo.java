package no.jonasandersen.admin.adapter.out.linode.model.api.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;

@Entity
@Table(name = "linode_volume", schema = "admin")
public class LinodeVolumeDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String label;
  private String status;
  private Long linodeId;

  public LinodeVolumeDbo() {
  }

  public LinodeVolumeDbo(Long id, String label, String status,
      Long linodeId) {
    this.id = id;
    this.label = label;
    this.status = status;
    this.linodeId = linodeId;
  }

  public LinodeVolume toDomain() {
    return new LinodeVolume(VolumeId.from(id), label, status, LinodeId.from(linodeId));
  }

  public Long id() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String label() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String status() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long linodeId() {
    return linodeId;
  }

  public void setLinodeId(Long linodeId) {
    this.linodeId = linodeId;
  }
}