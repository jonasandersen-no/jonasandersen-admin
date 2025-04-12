package no.jonasandersen.admin.adapter.in.test;

import java.util.UUID;
import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.TestUseCase;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestRestController {

  private final EventStore<TestId, TestEvent, Test> eventStore;
  private final TestUseCase testUseCase;

  public TestRestController(EventStore<TestId, TestEvent, Test> eventStore) {
    this.eventStore = eventStore;
    this.testUseCase = new TestUseCase(eventStore);
  }

  @GetMapping
  String findById(@RequestParam UUID id) {
    Test test = testUseCase.findById(new TestId(id));
    if (test != null) {
      return eventStore.allEvents(new TestId(id)).toList().toString();
    }
    return "Could not find test by id: " + id;
  }

  @PostMapping
  void testEvent(@RequestParam UUID id) {
    testUseCase.testEvent(new TestId(id));
  }
}
