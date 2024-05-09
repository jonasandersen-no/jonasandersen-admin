package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeVolumeDbo;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;

public class DatabaseServerApi implements ServerApi {

  private final JdbcLinodeInstanceRepository repository;
  private final JdbcLinodeVolumeRepository volumeRepository;

  public DatabaseServerApi(JdbcLinodeInstanceRepository repository,
      JdbcLinodeVolumeRepository volumeRepository) {
    this.repository = repository;
    this.volumeRepository = volumeRepository;
  }

  @Override
  public LinodeInstance createInstance(String label, String tags) {
    throw new UnsupportedOperationException();
  }

  @Override
  public LinodeInstance createInstance(InstanceDetails instanceDetails) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    return volumeRepository.findAll()
        .stream()
        .map(LinodeVolumeDbo::toDomain)
        .toList();
  }

  @Override
  public List<LinodeInstance> getInstances() {

    return repository.findAll()
        .stream()
        .map(LinodeInstanceDbo::toDomain)
        .toList();

  }

  @Override
  public LinodeInstance getInstanceById(LinodeId linodeId) {
    return repository.findById(linodeId.id())
        .map(LinodeInstanceDbo::toDomain)
        .orElseThrow();
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return volumeRepository.findByLinodeId(linodeId.id())
        .stream()
        .map(LinodeVolumeDbo::toDomain)
        .toList();
  }
}
