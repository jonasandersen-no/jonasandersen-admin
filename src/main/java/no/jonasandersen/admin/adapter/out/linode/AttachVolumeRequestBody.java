package no.jonasandersen.admin.adapter.out.linode;

import com.fasterxml.jackson.annotation.JsonProperty;

class AttachVolumeRequestBody {

  @JsonProperty("linode_id")
  private Long linodeId;
  @JsonProperty("config_id")
  private Long configId;
  @JsonProperty("persist_across_boots")
  private Boolean persistAcrossBoots;

  public Long getLinodeId() {
    return linodeId;
  }

  public void setLinodeId(Long linodeId) {
    this.linodeId = linodeId;
  }

  public Long getConfigId() {
    return configId;
  }

  public void setConfigId(Long configId) {
    this.configId = configId;
  }

  public Boolean getPersistAcrossBoots() {
    return persistAcrossBoots;
  }

  public void setPersistAcrossBoots(Boolean persistAcrossBoots) {
    this.persistAcrossBoots = persistAcrossBoots;
  }
}
