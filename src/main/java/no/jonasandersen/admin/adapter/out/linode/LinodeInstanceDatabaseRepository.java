package no.jonasandersen.admin.adapter.out.linode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeStoredInfo;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.SaveLinodeInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

public class LinodeInstanceDatabaseRepository {

  private static final Logger log = LoggerFactory.getLogger(LinodeInstanceDatabaseRepository.class);
  private final LinodeInstanceRepository repository;

  public static LinodeInstanceDatabaseRepository create(JpaLinodeInstanceRepository repository) {
    return new LinodeInstanceDatabaseRepository(new RealInstanceRepository(repository));
  }

  public static LinodeInstanceDatabaseRepository createNull() {
    return new LinodeInstanceDatabaseRepository(new StubLinodeInstanceRepository());
  }

  private LinodeInstanceDatabaseRepository(LinodeInstanceRepository repository) {
    this.repository = repository;
  }

  public LinodeInstance createInstance(InstanceDetails instanceDetails) {
    throw new UnsupportedOperationException();
  }

  public List<LinodeStoredInfo> getInstances() {

    return repository.findAll()
        .stream()
        .map(LinodeInstanceDbo::toDomain)
        .toList();

  }

  public LinodeStoredInfo getInstanceById(LinodeId linodeId) {
    return repository.findById(linodeId.id())
        .map(LinodeInstanceDbo::toDomain)
        .orElseThrow();
  }

  @EventListener
  public void onSaveEvent(SaveLinodeInstanceEvent event) {
    log.info("Received save event: {}", event);
    save(event);
  }

  void save(SaveLinodeInstanceEvent event) {
    Long id = event.id();
    if (id != null) {
      Optional<LinodeInstanceDbo> found = repository.findById(id);
      if (found.isPresent()) {
        log.info("Updating existing instance with id: {}", id);
        LinodeInstanceDbo dbo = found.get();
        dbo.setLinodeId(event.linodeId().id());
        dbo.setSubDomain(null);
        dbo.setServerType(null);
        dbo.setCreatedBy(event.createdBy());
        repository.save(dbo);
        return;
      }
    }

    log.info("Saving new instance {}", event);
    LinodeInstanceDbo dbo = new LinodeInstanceDbo();
    dbo.setLinodeId(event.linodeId().id());
    dbo.setCreatedDate(LocalDateTime.now());
    dbo.setSubDomain(null);
    dbo.setServerType(null);
    dbo.setCreatedBy(event.createdBy());
    repository.save(dbo);
  }

  public void deleteAll() {
    repository.deleteAll();
  }

  private interface LinodeInstanceRepository {

    List<LinodeInstanceDbo> findAll();

    Optional<LinodeInstanceDbo> findById(Long id);

    LinodeInstanceDbo save(LinodeInstanceDbo dbo);

    void deleteAll();
  }


  private static class RealInstanceRepository implements LinodeInstanceRepository {

    private final JpaLinodeInstanceRepository repository;

    private RealInstanceRepository(JpaLinodeInstanceRepository repository) {
      this.repository = repository;
    }

    @Override
    public List<LinodeInstanceDbo> findAll() {
      return repository.findAll();
    }

    @Override
    public Optional<LinodeInstanceDbo> findById(Long id) {
      return repository.findById(id);
    }

    @Override
    public LinodeInstanceDbo save(LinodeInstanceDbo dbo) {
      return repository.save(dbo);
    }

    @Override
    public void deleteAll() {
      repository.deleteAll();
    }

  }

  private static class StubLinodeInstanceRepository implements LinodeInstanceRepository {

    private final Map<Long, LinodeInstanceDbo> instances = new HashMap<>();
    private long id = 0;

    @Override
    public List<LinodeInstanceDbo> findAll() {
      return List.copyOf(instances.values());
    }

    @Override
    public Optional<LinodeInstanceDbo> findById(Long id) {
      return Optional.ofNullable(instances.get(id));
    }

    @Override
    public LinodeInstanceDbo save(LinodeInstanceDbo dbo) {
      if (dbo.id() != null) {
        instances.put(dbo.id(), dbo);
        return dbo;
      }

      long currentId = id++;
      dbo.setId(currentId);
      instances.put(currentId, dbo);
      return dbo;
    }

    @Override
    public void deleteAll() {
      instances.clear();
    }
  }

}
