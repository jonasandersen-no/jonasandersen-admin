package no.jonasandersen.admin.adapter.out.savefile;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("save_file")
public class SaveFileDbo {

  @Id
  private Long id;

  private String name;

  private Long owner;

  private Long volumeId;

  private Long linodeId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getOwner() {
    return owner;
  }

  public void setOwner(Long owner) {
    this.owner = owner;
  }

  public Long getVolumeId() {
    return volumeId;
  }

  public void setVolumeId(Long volumeId) {
    this.volumeId = volumeId;
  }

  public Long getLinodeId() {
    return linodeId;
  }

  public void setLinodeId(Long linodeId) {
    this.linodeId = linodeId;
  }

  public void validate() {
    if (name == null) {
      throw new IllegalStateException("name is null");
    }

    if (owner == null) {
      throw new IllegalStateException("owner is null");
    }
  }
}
