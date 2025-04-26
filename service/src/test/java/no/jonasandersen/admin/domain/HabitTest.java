package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class HabitTest {

  @Nested
  class CommandsGenerateEvents {

    @Test
    void createHabitCommandGeneratesHabitCreatedEvent() {
      UUID aggregateId = UUID.randomUUID();

      Habit habit = Habit.create(aggregateId, "Drink water", "Daily");

      assertThat(habit.uncommittedEvents())
          .containsExactly(new HabitCreatedEvent(aggregateId, 1, "Drink water", "Daily"));
    }
  }

  @Nested
  class EventsGenerateState {

    @Test
    void habitCreatedEventUpdatesIdAndNameAndGoal() {
      UUID aggregateId = UUID.randomUUID();

      Habit habit = new Habit();

      habit.apply(new HabitCreatedEvent(aggregateId, 1, "Drink water", "Daily"));

      assertThat(habit.getId()).isEqualTo(new HabitId(aggregateId));
      assertThat(habit.getVersion()).isEqualTo(1);
      assertThat(habit.getName()).isEqualTo("Drink water");
      assertThat(habit.getGoal()).isEqualTo("Daily");
    }
  }
}
