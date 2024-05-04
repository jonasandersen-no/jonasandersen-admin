package no.jonasandersen.admin.core.domain;

import java.util.List;

public record LinodeInstance(LinodeId linodeId, List<String> ip, String status, String label,
                             List<String> tags, List<String> volumeNames) {

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
}
