package no.jonasandersen.admin.core.minecraft.port;

import java.util.List;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.domain.InstanceDetails;

public interface ServerApi {

  /**
   * @deprecated use {@link #createInstance(InstanceDetails)} instead
   */
  @Deprecated
  LinodeInstance createInstance(String label, String tags);

  LinodeInstance createInstance(InstanceDetails instanceDetails);

  List<LinodeVolume> getVolumes();

  List<LinodeInstance> getInstances();

  LinodeInstance getInstanceById(LinodeId linodeId);

  List<LinodeVolume> getVolumesByInstance(LinodeId linodeId);
}
