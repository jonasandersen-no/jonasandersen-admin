package no.jonasandersen.admin.domain;

import java.util.UUID;

public record HabitCreatedEvent(UUID aggregateId, int version, String name, String goal)
    implements HabitEvent {}
