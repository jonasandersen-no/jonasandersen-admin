package no.jonasandersen.admin.adapter.out.store;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventStoreRepository implements EventStoreRepository {

  private final JdbcEventRepository repository;

  public DefaultEventStoreRepository(JdbcEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public void saveEvent(
      UUID aggregateRootId, Instant recordedAt, long version, String eventType, String content) {

    JdbcEvent event = new JdbcEvent();
    event.setEventId(UUID.randomUUID());
    event.setAggregateRootId(aggregateRootId);
    event.setVersion(version);
    event.setEventType(eventType);
    event.setRecordedAt(recordedAt);
    event.setContent(content);

    repository.save(event);
  }

  @Override
  public List<DatabaseEvent> findByAggregateRootId(UUID id) {

    List<JdbcEvent> events = repository.findByAggregateRootIdOrderByVersion(id);

    return events.stream()
        .map(
            event ->
                new DatabaseEvent(
                    event.getAggregateRootId(),
                    event.getRecordedAt(),
                    event.getVersion(),
                    event.getEventType(),
                    event.getContent()))
        .toList();
  }
}
