package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeVolumeDbo;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.domain.InstanceDetails;

public class DatabaseServerApi {

  private final JdbcLinodeInstanceRepository repository;
  private final JdbcLinodeVolumeRepository volumeRepository;

  public DatabaseServerApi(JdbcLinodeInstanceRepository repository,
      JdbcLinodeVolumeRepository volumeRepository) {
    this.repository = repository;
    this.volumeRepository = volumeRepository;
  }

  public LinodeInstance createInstance(InstanceDetails instanceDetails) {
    throw new UnsupportedOperationException();
  }

  public List<LinodeVolume> getVolumes() {
    return volumeRepository.findAll()
        .stream()
        .map(LinodeVolumeDbo::toDomain)
        .toList();
  }

  public List<LinodeInstance> getInstances() {

    return repository.findAll()
        .stream()
        .map(LinodeInstanceDbo::toDomain)
        .toList();

  }

  public LinodeInstance getInstanceById(LinodeId linodeId) {
    return repository.findById(linodeId.id())
        .map(LinodeInstanceDbo::toDomain)
        .orElseThrow();
  }

  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return volumeRepository.findByLinodeId(linodeId.id())
        .stream()
        .map(LinodeVolumeDbo::toDomain)
        .toList();
  }
}
