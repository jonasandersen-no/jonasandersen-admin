package no.jonasandersen.admin.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;
import org.springframework.stereotype.Component;

@Component
public class TestProjector {

  private final EventStore<TestId, TestEvent, Test> eventStore;

  public TestProjector(EventStore<TestId, TestEvent, Test> eventStore) {
    this.eventStore = eventStore;
  }

  public TestView findAllTestIds(TestId id) {
    Stream<TestEvent> events = eventStore.allEvents(id);

    List<String> strings = new ArrayList<>();
    events.forEach(
        testEvent -> {
          strings.add(testEvent.id().id().toString());
        });

    return new TestView(strings);
  }

  public record TestView(List<String> ids) {}
}
