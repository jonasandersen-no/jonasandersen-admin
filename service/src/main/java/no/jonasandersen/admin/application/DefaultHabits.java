package no.jonasandersen.admin.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import no.jonasandersen.admin.application.port.Habits;
import no.jonasandersen.admin.domain.Habit;
import no.jonasandersen.admin.domain.HabitCompletedEvent;
import no.jonasandersen.admin.domain.HabitCreatedEvent;
import no.jonasandersen.admin.domain.HabitEvent;
import no.jonasandersen.admin.domain.HabitId;
import org.springframework.stereotype.Service;

@Service
public class DefaultHabits implements Habits {

  private final EventStore<HabitId, HabitEvent, Habit> eventStore;

  public DefaultHabits(EventStore<HabitId, HabitEvent, Habit> eventStore) {
    this.eventStore = eventStore;
  }

  @Override
  public Habit create(String name, String goal) {
    UUID habitId = UUID.randomUUID();
    Habit habit = Habit.create(habitId, name, goal);
    eventStore.save(habit);
    return habit;
  }

  @Override
  public List<Habit> findAll() {
    Stream<HabitEvent> events = eventStore.allEvents(Habit.class.getName());

    events.forEach(habitEvent -> {
      switch (habitEvent) {
        case HabitCreatedEvent(UUID aggregateId, int version, String name, String goal) -> {
          Habit habit = Habit.reconstitute(List.of(habitEvent));
          habit.setId(new HabitId(aggregateId));
          habit.version = version;
        }
        case HabitCompletedEvent(_, int version, LocalDateTime completionDate) -> {
          Habit habit = Habit.reconstitute(List.of(habitEvent));
          habit.version = version;
        }
      }
    })

    return new ArrayList<>(habits);
  }

  @Override
  public void complete(UUID habitId, LocalDateTime completionTime) {
    habits.stream()
        .filter(habit -> habit.getId().id().equals(habitId))
        .findFirst()
        .ifPresent(habit -> habit.complete(habitId, completionTime));
  }
}