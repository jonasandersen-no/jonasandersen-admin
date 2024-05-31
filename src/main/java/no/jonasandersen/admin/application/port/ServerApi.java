package no.jonasandersen.admin.application.port;

import java.util.List;
import java.util.Optional;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeVolume;
import no.jonasandersen.admin.domain.VolumeId;

public interface ServerApi {

  LinodeInstance createInstance(InstanceDetails instanceDetails);

  List<LinodeVolume> getVolumes();

  List<LinodeInstance> getInstances();

  Optional<LinodeInstance> findInstanceById(LinodeId linodeId);

  List<LinodeVolume> getVolumesByInstance(LinodeId linodeId);

  void attachVolume(LinodeId linodeId, VolumeId volumeId);
}
