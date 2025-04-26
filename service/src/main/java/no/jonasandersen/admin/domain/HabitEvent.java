package no.jonasandersen.admin.domain;

import java.util.UUID;

public sealed interface HabitEvent extends Event permits HabitCreatedEvent {
  UUID aggregateId();

  int version();
}
