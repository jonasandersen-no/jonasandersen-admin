package no.jonasandersen.admin.adapter.out.linode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record LinodeVolume(int id, String status, String label, LocalDateTime created,
                           LocalDateTime updated,
                           @JsonProperty("filesystem_path") String fileSystemPath, int size,
                           @JsonProperty("linode_id") Integer linodeId,
                           @JsonProperty("linode_label") String linodeLabel,
                           String region, List<String> tags,
                           @JsonProperty("hardware_type") String hardwareType) {

//  public Volume toDomain() {
//    Volume volume = new Volume();
//    volume.setId(new VolumeId((long) id));
//    volume.setLabel(label);
//    volume.setStatus(status);
//    if (linodeId != null) {
//      volume.setLinodeId(new LinodeId((long) linodeId));
//    }
//
//    return volume;
//  }

}
