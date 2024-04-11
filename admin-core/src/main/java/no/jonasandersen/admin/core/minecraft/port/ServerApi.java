package no.jonasandersen.admin.core.minecraft.port;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.domain.MinecraftInstance;

public interface ServerApi {

  MinecraftInstance listServerInfo();

  List<LinodeVolume> getVolumes();

  List<LinodeInstance> getInstances();

  List<LinodeVolume> getVolumesByInstance(LinodeId linodeId);
}
