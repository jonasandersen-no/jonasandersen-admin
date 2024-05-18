package no.jonasandersen.admin.application.port;

public interface EventPublisher {

  void publishEvent(Object event);

}
