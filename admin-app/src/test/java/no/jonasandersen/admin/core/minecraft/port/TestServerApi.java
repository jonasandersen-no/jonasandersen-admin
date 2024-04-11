package no.jonasandersen.admin.core.minecraft.port;


import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public class TestServerApi implements ServerApi {


  @Override
  public MinecraftInstance listServerInfo() {
    return new MinecraftInstance("minecraft-auto-config-1", "127.0.0.1", "running");
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
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return List.of();
  }
}

