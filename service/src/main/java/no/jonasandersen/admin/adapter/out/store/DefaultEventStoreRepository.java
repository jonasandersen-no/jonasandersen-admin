package no.jonasandersen.admin.adapter.out.store;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import org.occurrent.eventstore.api.blocking.EventStream;
import org.occurrent.eventstore.mongodb.spring.blocking.SpringMongoEventStore;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventStoreRepository implements EventStoreRepository {

  private final SpringMongoEventStore eventStore;

  public DefaultEventStoreRepository(SpringMongoEventStore eventStore) {
    this.eventStore = eventStore;
  }

  @Override
  public void saveEvent(
      UUID aggregateRootId, Instant recordedAt, int eventId, String eventType, String content) {

    CloudEvent event =
        CloudEventBuilder.v1()
            .withId(eventId + "-" + UUID.randomUUID())
            .withSource(URI.create("urn:jonasandersen:admin"))
            .withType(eventType)
            .withTime(recordedAt.atOffset(ZoneOffset.UTC))
            .withData(content.getBytes())
            .build();

    eventStore.write(aggregateRootId.toString(), event);
  }

  @Override
  public List<DatabaseEvent> findByAggregateRootId(UUID id) {
    EventStream<CloudEvent> cloudEvents = eventStore.read(id.toString());

    return cloudEvents.eventList().stream()
        .map(
            cloudEvent -> {
              return new DatabaseEvent(
                  id,
                  cloudEvent.getTime().toInstant(),
                  Integer.parseInt(cloudEvent.getId().split("-")[0]),
                  cloudEvent.getType(),
                  new String(cloudEvent.getData().toBytes()));
            })
        .toList();
  }
}
