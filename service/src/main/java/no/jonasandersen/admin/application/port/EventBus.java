package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.Event;

@FunctionalInterface
public interface EventBus {

  void publish(Event event);
}
