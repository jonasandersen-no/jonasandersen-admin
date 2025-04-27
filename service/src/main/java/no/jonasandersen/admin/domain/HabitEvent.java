package no.jonasandersen.admin.domain;

import java.util.UUID;

public sealed interface HabitEvent extends Event permits HabitCompletedEvent, HabitCreatedEvent {
  UUID aggregateId();

  int version();
}
