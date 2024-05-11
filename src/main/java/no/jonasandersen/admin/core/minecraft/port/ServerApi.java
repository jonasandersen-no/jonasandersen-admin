package no.jonasandersen.admin.core.minecraft.port;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.domain.InstanceDetails;

public interface ServerApi {

  LinodeInstance createInstance(InstanceDetails instanceDetails);

  List<LinodeVolume> getVolumes();

  List<LinodeInstance> getInstances();

  Optional<LinodeInstance> findInstanceById(LinodeId linodeId);

  List<LinodeVolume> getVolumesByInstance(LinodeId linodeId);
}
