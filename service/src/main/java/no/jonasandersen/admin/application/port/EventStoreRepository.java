package no.jonasandersen.admin.application.port;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.adapter.out.store.EventDto;

public interface EventStoreRepository {

  record DatabaseEvent(
      UUID aggregateRootId, Instant recordedAt, int eventId, String eventType, String content) {}

  void saveEvent(
      UUID aggregateRootId, Instant recordedAt, int eventId, String eventType, String content);

  List<DatabaseEvent> findByAggregateRootId(UUID id);

  List<DatabaseEvent> findAllByEventType(String eventType);
}
