package no.jonasandersen.admin.core.minecraft;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.LinodeServerApi;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class LinodeVolumeService {

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
}
