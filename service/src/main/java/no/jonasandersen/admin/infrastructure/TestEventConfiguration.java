package no.jonasandersen.admin.infrastructure;

import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.port.EventBus;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import no.jonasandersen.admin.domain.Test;
import no.jonasandersen.admin.domain.TestEvent;
import no.jonasandersen.admin.domain.TestId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestEventConfiguration {

  @Bean
  EventStore<TestId, TestEvent, Test> testEventStore(
      EventStoreRepository repository, EventBus eventBus) {
    return EventStore.forTests(repository, eventBus);
  }
}
