package no.jonasandersen.admin.adapter.in.test;

import java.util.UUID;
import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.TestProjector;
import no.jonasandersen.admin.application.TestProjector.TestView;
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

  private final TestUseCase testUseCase;
  private final TestProjector testProjector;

  public TestRestController(
      EventStore<TestId, TestEvent, Test> eventStore, TestProjector testProjector) {
    this.testUseCase = new TestUseCase(eventStore);
    this.testProjector = testProjector;
  }

  @GetMapping
  TestView findById(@RequestParam UUID id) {
    return testProjector.findAllTestIds(new TestId(id));
  }

  @PostMapping
  void testEvent(@RequestParam UUID id) {
    testUseCase.testEvent(new TestId(id));
  }
}
