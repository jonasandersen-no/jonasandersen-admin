package no.jonasandersen.admin.adapter.out.linode.model.api.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import no.jonasandersen.admin.core.domain.LinodeInstance;

@Entity
@Table(name = "linode_instance", schema = "admin")
public class LinodeInstanceDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long linodeId;
  private String createdBy;
  private LocalDateTime createdDate;
  private String serverType;
  private String subDomain;

  public LinodeInstanceDbo() {
  }

  public Long id() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long linodeId() {
    return linodeId;
  }

  public void setLinodeId(Long linodeId) {
    this.linodeId = linodeId;
  }

  public String createdBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime createdDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String serverType() {
    return serverType;
  }

  public void setServerType(String serverType) {
    this.serverType = serverType;
  }

  public String subDomain() {
    return subDomain;
  }

  public void setSubDomain(String subDomain) {
    this.subDomain = subDomain;
  }

  public LinodeInstance toDomain() {
    return null;
  }
}

