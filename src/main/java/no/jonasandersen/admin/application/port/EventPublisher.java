package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.OutputTracker;

public interface EventPublisher {

  OutputTracker<Object> track();

  void publishEvent(Object event);

}
