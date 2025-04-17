package no.jonasandersen.admin.adapter.out.eventbus;

import no.jonasandersen.admin.application.port.EventBus;
import no.jonasandersen.admin.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationEventPublisherEventBus implements EventBus {

  private static final Logger log =
      LoggerFactory.getLogger(SpringApplicationEventPublisherEventBus.class);
  private final ApplicationEventPublisher publisher;

  public SpringApplicationEventPublisherEventBus(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(Event event) {
    log.info("Publishing event {}", event);
    publisher.publishEvent(event);
  }
}
