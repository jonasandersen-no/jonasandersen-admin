package no.jonasandersen.admin.application;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import no.jonasandersen.admin.adapter.out.store.EventDto;
import no.jonasandersen.admin.application.port.EventBus;
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
  private final EventBus eventBus;

  private EventStore(
      Function<List<EVENT>, AGGREGATE> eventsToAggregate,
      EventStoreRepository repository,
      EventBus eventBus) {
    this.eventsToAggregate = eventsToAggregate;
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public static EventStore<SaveFileId, SaveFileEvent, SaveFile> forSaveFiles() {
    return forSaveFiles(new InMemoryEventStoreRepository(), _ -> {});
  }

  public static EventStore<SaveFileId, SaveFileEvent, SaveFile> forSaveFiles(
      EventStoreRepository repository, EventBus eventBus) {
    return new EventStore<>(SaveFile::reconstitute, repository, eventBus);
  }

  public static EventStore<TestId, TestEvent, Test> forTests() {
    return forTests(new InMemoryEventStoreRepository(), _ -> {});
  }

  public static EventStore<TestId, TestEvent, Test> forTests(
      EventStoreRepository repository, EventBus eventBus) {
    return new EventStore<>(Test::reconstitute, repository, eventBus);
  }

  public void save(AGGREGATE aggregate) {
    if (aggregate.getId() == null) {
      throw new IllegalArgumentException("The Aggregate " + aggregate + " must have an ID");
    }
    List<EVENT> uncommittedEvents = aggregate.uncommittedEvents();

    List<EventDto<EVENT>> freshEventDtos =
        uncommittedEvents.stream()
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

    for (EVENT uncommittedEvent : uncommittedEvents) {
      eventBus.publish(uncommittedEvent);
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
