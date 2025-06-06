package no.jonasandersen.admin.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Habit extends EventSourcedAggregate<HabitEvent, HabitId> {

  private String name;
  private int version = 0;
  private String goal;
  private final List<LocalDateTime> completions = new ArrayList<>();

  public static Habit create(UUID aggregateId, String name, String goal) {
    Habit habit = new Habit();
    habit.enqueue(new HabitCreatedEvent(aggregateId, habit.version + 1, name, goal));
    return habit;
  }

  public void complete(UUID aggregateId, LocalDateTime completionTime) {

    boolean found = false;
    for (LocalDateTime completion : this.completions) {
      LocalDate localDate = LocalDate.from(completion);
      if (localDate.isEqual(completionTime.toLocalDate())) {
        found = true;
        break;
      }
    }

    if (found) {
      throw new HabitException("habit already completed for this date");
    }

    enqueue(new HabitCompletedEvent(aggregateId, this.version + 1, completionTime));
  }

  @Override
  protected void apply(HabitEvent event) {

    if (this.getId() != null && !this.getId().id().equals(event.aggregateId())) {
      throw new IllegalStateException("aggregate id mismatch");
    }

    if (this.version != event.version() - 1 && this.version != 0) {
      throw new IllegalStateException(
          "event version mismatch. Expected %s, got %s"
              .formatted(this.version + 1, event.version()));
    }

    switch (event) {
      case HabitCreatedEvent(UUID aggregateId, int version, String name, String goal) -> {
        setId(new HabitId(aggregateId));
        this.version = version;
        this.name = name;
        this.goal = goal;
      }
      case HabitCompletedEvent(_, int version, LocalDateTime completionDate) -> {
        this.version = version;
        this.completions.add(completionDate);
      }
    }
  }

  public static Habit reconstitute(HabitEvent... events) {
    return reconstitute(List.of(events));
  }

  public static Habit reconstitute(List<HabitEvent> events) {
    Habit habit = new Habit();
    for (HabitEvent event : events) {
      habit.apply(event);
    }
    return habit;
  }

  public String getName() {
    return name;
  }

  public int getVersion() {
    return this.version;
  }

  public String getGoal() {
    return goal;
  }

  public List<LocalDateTime> getCompletions() {
    return completions;
  }
}
