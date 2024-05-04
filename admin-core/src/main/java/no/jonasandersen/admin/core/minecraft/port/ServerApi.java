package no.jonasandersen.admin.core.minecraft.port;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;

public interface ServerApi {

  LinodeInstance createInstance(String label, String tags);

  List<LinodeVolume> getVolumes();

  List<LinodeInstance> getInstances();

  LinodeInstance getInstanceById(LinodeId linodeId);

  List<LinodeVolume> getVolumesByInstance(LinodeId linodeId);
}
