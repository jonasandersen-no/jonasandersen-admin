package no.jonasandersen.admin.domain;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test extends EventSourcedAggregate<TestEvent, TestId> {

  private static final Logger log = LoggerFactory.getLogger(Test.class);

  public Test() {}

  private Test(List<TestEvent> events) {
    events.forEach(this::apply);
  }

  @Override
  protected void apply(TestEvent event) {
    setId(event.id());
  }

  public static Test reconstitute(List<TestEvent> events) {
    return new Test(events);
  }

  public void doSomething(TestId testId) {
    log.info("I did something");
    enqueue(new TestEvent(testId));
  }
}
