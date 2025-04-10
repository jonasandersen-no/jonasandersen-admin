package no.jonasandersen.admin.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import no.jonasandersen.admin.adapter.out.store.EventDto;
import no.jonasandersen.admin.domain.Event;
import no.jonasandersen.admin.domain.EventSourcedAggregate;
import no.jonasandersen.admin.domain.Id;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;

public class EventStore<
    ID extends Id, EVENT extends Event, AGGREGATE extends EventSourcedAggregate<EVENT, ID>> {

  private final Map<ID, List<EventDto<EVENT>>> idToEventDtoMap = new HashMap<>();
  private final Function<List<EVENT>, AGGREGATE> eventsToAggregate;

  private EventStore(Function<List<EVENT>, AGGREGATE> eventsToAggregate) {
    this.eventsToAggregate = eventsToAggregate;
  }

  public static EventStore<TestId, TestEvent, Test> forTests() {
    return new EventStore<>(Test::reconstitute);
  }

  public void save(AGGREGATE aggregate) {
    if (aggregate.getId() == null) {
      throw new IllegalArgumentException("The Aggregate " + aggregate + " must have an ID");
    }
    List<EventDto<EVENT>> existingEventDtos =
        idToEventDtoMap.computeIfAbsent(aggregate.getId(), _ -> new ArrayList<>());
    List<EventDto<EVENT>> freshEventDtos =
        aggregate.uncommittedEvents().stream()
            .map(event -> EventDto.from(aggregate.getId().id(), 0, event))
            .toList();
    existingEventDtos.addAll(freshEventDtos);

    idToEventDtoMap.put(aggregate.getId(), existingEventDtos);
  }

  private AGGREGATE aggregateFromEvents(List<EventDto<EVENT>> existingEventDtos) {
    List<EVENT> events = existingEventDtos.stream().map(EventDto::toDomain).toList();
    return eventsToAggregate.apply(events);
  }

  public Optional<AGGREGATE> findById(ID id) {
    return Optional.ofNullable(idToEventDtoMap.get(id)).map(this::aggregateFromEvents);
  }

  public Stream<EVENT> allEvents() {
    return idToEventDtoMap.values().stream().flatMap(Collection::stream).map(EventDto::toDomain);
  }
}
