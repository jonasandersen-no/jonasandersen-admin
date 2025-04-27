package no.jonasandersen.admin.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record HabitCompletedEvent(UUID aggregateId, int version, LocalDateTime completionDate)
    implements HabitEvent {}
