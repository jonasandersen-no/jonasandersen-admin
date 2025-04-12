package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;

class TestUseCaseTest {

  @org.junit.jupiter.api.Test
  void name() {

    EventStore<TestId, TestEvent, Test> eventStore = EventStore.forTests();
    TestUseCase unit = new TestUseCase(eventStore);

    UUID id = UUID.randomUUID();
    TestId testId = new TestId(id);
    unit.testEvent(testId);
    unit.testEvent(testId);

    Test found = unit.findById(testId);

    assertThat(found).isNotNull();

    assertThat(eventStore.allEvents(new TestId(id))).hasSize(2);
  }
}
