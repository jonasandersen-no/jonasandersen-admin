package no.jonasandersen.admin.domain;

import java.util.UUID;

public class Habit extends EventSourcedAggregate<HabitEvent, HabitId> {

  private String name;
  private int version = 0;
  private String goal;

  public static Habit create(UUID aggregateId, String name, String goal) {
    Habit habit = new Habit();
    habit.enqueue(new HabitCreatedEvent(aggregateId, habit.version + 1, name, goal));
    return habit;
  }

  @Override
  protected void apply(HabitEvent event) {
    switch (event) {
      case HabitCreatedEvent(UUID aggregateId, int version, String name, String goal) -> {
        setId(new HabitId(aggregateId));
        this.version = version;
        this.name = name;
        this.goal = goal;
      }
    }
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
}
