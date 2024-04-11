package no.jonasandersen.admin.core;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class LinodeVolumeService {

  private final ServerApi serverApi;

  public LinodeVolumeService(ServerApi serverApi) {
    this.serverApi = serverApi;
  }

  public List<LinodeVolume> getVolumes() {
    return serverApi.getVolumes();
  }

  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return serverApi.getVolumesByInstance(linodeId);
  }
}
