package no.jonasandersen.admin.adapter;

import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.application.port.EventPublisher;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;

public class DefaultEventPublisher implements EventPublisher {

  private final ApplicationEventPublisher publisher;
  private final OutputListener<Object> outputListener = new OutputListener<>();

  public static DefaultEventPublisher create(ApplicationEventPublisher publisher) {
    return new DefaultEventPublisher(new RealApplicationEventPublisher(publisher));
  }

  public static DefaultEventPublisher createNull() {
    return new DefaultEventPublisher(new StubApplicationEventPublisher());
  }

  private DefaultEventPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public OutputTracker<Object> track() {
    return outputListener.createTracker();
  }

  @Override
  public void publishEvent(Object event) {
    outputListener.track(event);
    publisher.publishEvent(event);
  }

  private record RealApplicationEventPublisher(ApplicationEventPublisher publisher) implements
      ApplicationEventPublisher {

    @Override
    public void publishEvent(@NotNull Object event) {
      publisher.publishEvent(event);
    }
  }

  private static class StubApplicationEventPublisher implements ApplicationEventPublisher {

    @Override
    public void publishEvent(Object event) {

    }
  }
}
