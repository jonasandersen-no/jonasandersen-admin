package no.jonasandersen.admin.application;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import no.jonasandersen.admin.adapter.out.store.EventDto;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import no.jonasandersen.admin.application.port.InMemoryEventStoreRepository;
import no.jonasandersen.admin.domain.Event;
import no.jonasandersen.admin.domain.EventSourcedAggregate;
import no.jonasandersen.admin.domain.Id;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFileEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;

public class EventStore<
    ID extends Id, EVENT extends Event, AGGREGATE extends EventSourcedAggregate<EVENT, ID>> {

  private final Function<List<EVENT>, AGGREGATE> eventsToAggregate;
  private final EventStoreRepository repository;

  private EventStore(
      Function<List<EVENT>, AGGREGATE> eventsToAggregate, EventStoreRepository repository) {
    this.eventsToAggregate = eventsToAggregate;
    this.repository = repository;
  }

  public static EventStore<SaveFileId, SaveFileEvent, SaveFile> forSaveFiles() {
    return forSaveFiles(new InMemoryEventStoreRepository());
  }

  public static EventStore<SaveFileId, SaveFileEvent, SaveFile> forSaveFiles(
      EventStoreRepository repository) {
    return new EventStore<>(SaveFile::reconstitute, repository);
  }

  public static EventStore<TestId, TestEvent, Test> forTests() {
    return forTests(new InMemoryEventStoreRepository());
  }

  public static EventStore<TestId, TestEvent, Test> forTests(EventStoreRepository repository) {
    return new EventStore<>(Test::reconstitute, repository);
  }

  public void save(AGGREGATE aggregate) {
    if (aggregate.getId() == null) {
      throw new IllegalArgumentException("The Aggregate " + aggregate + " must have an ID");
    }
    List<EventDto<EVENT>> freshEventDtos =
        aggregate.uncommittedEvents().stream()
            .map(event -> EventDto.from(aggregate.getId().id(), 0, event))
            .toList();
    for (EventDto<EVENT> freshEventDto : freshEventDtos) {
      repository.saveEvent(
          aggregate.getId().id(),
          freshEventDto.getRecordedAt(),
          freshEventDto.getEventId(),
          freshEventDto.getEventType(),
          freshEventDto.getJson());
    }
  }

  private AGGREGATE aggregateFromEvents(List<EventDto<EVENT>> existingEventDtos) {
    List<EVENT> events = existingEventDtos.stream().map(EventDto::toDomain).toList();
    return eventsToAggregate.apply(events);
  }

  public Optional<AGGREGATE> findById(ID id) {
    List<EventDto<EVENT>> events =
        repository.findByAggregateRootId(id.id()).stream()
            .map(
                databaseEvent ->
                    new EventDto<EVENT>(
                        databaseEvent.aggregateRootId(),
                        databaseEvent.eventId(),
                        databaseEvent.eventType(),
                        databaseEvent.content()))
            .toList();

    return Optional.ofNullable(aggregateFromEvents(events));
  }

  public Stream<EVENT> allEvents(ID id) {
    List<EventDto<EVENT>> events =
        repository.findByAggregateRootId(id.id()).stream()
            .map(
                databaseEvent ->
                    new EventDto<EVENT>(
                        databaseEvent.aggregateRootId(),
                        databaseEvent.eventId(),
                        databaseEvent.eventType(),
                        databaseEvent.content()))
            .toList();

    return events.stream().map(EventDto::toDomain);
  }
}
