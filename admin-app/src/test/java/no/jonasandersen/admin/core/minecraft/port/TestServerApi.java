package no.jonasandersen.admin.core.minecraft.port;


import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public class TestServerApi implements ServerApi {


  private final MinecraftInstance minecraftInstance = new MinecraftInstance(
      "minecraft-auto-config-1",
      "127.0.0.1", "running");
  private LinodeInstance linodeInstance = new LinodeInstance(LinodeId.from(1L),
      List.of("127.0.0.1"), "running",
      "minecraft-auto-config-1",
      List.of("minecraft"),
      List.of("minecraft-auto-config-1-volume-1", "minecraft-auto-config-1-volume-2"));

  @Override
  public MinecraftInstance listServerInfo() {
    return minecraftInstance;
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    return List.of();
  }

  @Override
  public List<LinodeInstance> getInstances() {
    return List.of(linodeInstance);
  }

  @Override
  public LinodeInstance getInstanceById(LinodeId linodeId) {
    return linodeInstance;
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return List.of();
  }
}

