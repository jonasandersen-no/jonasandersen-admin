package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.domain.VolumeId;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinodeVolumeService {

  private static final Logger log = LoggerFactory.getLogger(LinodeVolumeService.class);
  private final ServerApi serverApi;

  public static LinodeVolumeService create(ServerApi serverApi) {
    return new LinodeVolumeService(serverApi);
  }

  public static LinodeVolumeService createNull() {
    return new LinodeVolumeService(LinodeServerApi.createNull());
  }

  private LinodeVolumeService(ServerApi serverApi) {
    this.serverApi = serverApi;
  }

  public List<LinodeVolume> getVolumes() {
    return serverApi.getVolumes();
  }

  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return serverApi.getVolumesByInstance(linodeId);
  }

  public void attachVolume(LinodeId linodeId, VolumeId volumeId) {
    log.info("Attaching volume {} to linode {}", volumeId, linodeId);
    serverApi.attachVolume(linodeId, volumeId);
  }
}
