package no.jonasandersen.admin.infrastructure;

import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.port.EventBus;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import no.jonasandersen.admin.domain.Habit;
import no.jonasandersen.admin.domain.HabitEvent;
import no.jonasandersen.admin.domain.HabitId;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFileEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HabitConfiguration {

  @Bean
  EventStore<HabitId, HabitEvent, Habit> habitEventStore(
      EventStoreRepository repository, EventBus eventBus) {
    return EventStore.forHabits(repository, eventBus);
  }
}
