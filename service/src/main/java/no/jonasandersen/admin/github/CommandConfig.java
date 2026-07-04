package no.jonasandersen.admin.github;

import java.util.HashMap;
import java.util.Map;

public class CommandConfig {
  private String apiVersion;
  private String kind;
  private Metadata metadata;
  private String type;
  private Map<String, Object> spec = new HashMap<>();

  // getters and setters

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, Object> getSpec() {
    return spec;
  }

  public void setSpec(Map<String, Object> spec) {
    this.spec = spec;
  }

  @Override
  public String toString() {
    return "CommandConfig{"
        + "apiVersion='"
        + apiVersion
        + '\''
        + ", kind='"
        + kind
        + '\''
        + ", metadata="
        + metadata
        + ", type='"
        + type
        + '\''
        + ", spec="
        + spec
        + '}';
  }

  public String getName() {
    return metadata.getName();
  }
}
