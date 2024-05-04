package no.jonasandersen.admin.core.minecraft.port;


import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;

public class TestServerApi implements ServerApi {


  @Override
  public LinodeInstance createInstance(String label, String tags) {
    return null;
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    return List.of();
  }

  @Override
  public List<LinodeInstance> getInstances() {
    return List.of();
  }

  @Override
  public LinodeInstance getInstanceById(LinodeId linodeId) {
    return null;
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return List.of();
  }
}

