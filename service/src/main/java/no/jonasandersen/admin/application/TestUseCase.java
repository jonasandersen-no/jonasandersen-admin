package no.jonasandersen.admin.application;

import java.util.Optional;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;

public class TestUseCase {
  private final EventStore<TestId, TestEvent, Test> eventStore;

  public TestUseCase(EventStore<TestId, TestEvent, Test> eventStore) {
    this.eventStore = eventStore;
  }

  public Test findById(TestId testId) {
    Optional<Test> byId = eventStore.findById(testId);

    return byId.orElse(null);
  }

  public void testEvent(TestId testId) {

    var test = eventStore.findById(testId).orElse(new Test());

    test.doSomething(testId);

    eventStore.save(test);
  }
}
