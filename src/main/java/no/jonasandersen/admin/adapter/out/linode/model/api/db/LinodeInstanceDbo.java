package no.jonasandersen.admin.adapter.out.linode.model.api.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeSpecs;

@Entity
@Table(name = "linode_instance", schema = "admin")
public class LinodeInstanceDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String ips;
  private String status;
  private String label;
  private String tags;
  private String volumeNames;

  public LinodeInstanceDbo() {
  }

  public LinodeInstanceDbo(Long id, String ips, String status, String label,
      String tags, String volumeNames) {
    this.id = id;
    this.ips = ips;
    this.status = status;
    this.label = label;
    this.tags = tags;
    this.volumeNames = volumeNames;
  }

  public Long id() {
    return id;
  }

  public String ips() {
    return ips;
  }

  public String status() {
    return status;
  }

  public String label() {
    return label;
  }

  public String tags() {
    return tags;
  }

  public String volumeNames() {
    return volumeNames;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIps(String ips) {
    this.ips = ips;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public void setVolumeNames(String volumeNames) {
    this.volumeNames = volumeNames;
  }

  public LinodeInstance toDomain() {
    return new LinodeInstance(LinodeId.from(id), List.of(ips.split(",")), status, label,
        List.of(tags.split(",")), List.of(volumeNames.split(",")), new LinodeSpecs(0));
  }
}

