package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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

    @Test
    void completeHabitCommandGeneratesHabitCompletedEvent() {
      UUID aggregateId = UUID.randomUUID();
      LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

      Habit habit = Habit.reconstitute(List.of());

      habit.complete(aggregateId, now);

      assertThat(habit.uncommittedEvents())
          .containsExactly(new HabitCompletedEvent(aggregateId, 1, now));
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

  @Test
  void habitCompletedEventAddsCurrentDateToListOfCompletions() {
    UUID aggregateId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

    Habit habit = new Habit();

    habit.apply(new HabitCompletedEvent(aggregateId, 1, now));

    assertThat(habit.getVersion()).isEqualTo(1);
    assertThat(habit.getCompletions()).containsExactly(now);
  }

  @Test
  void eventVersionShouldOneHigherThanAggregate() {
    Habit habit = new Habit();

    assertThat(habit.getVersion()).isEqualTo(0);

    habit.apply(new HabitCreatedEvent(UUID.randomUUID(), habit.getVersion() + 1, "name", "goal"));

    assertThat(habit.getVersion()).isEqualTo(1);
  }

  @Test
  void applyFailsWhenEventIsNotInCorrectOrder() {

    UUID aggregateId = UUID.randomUUID();
    Habit habit = Habit.create(aggregateId, "name", "goal");
    assertThat(habit.getVersion()).isEqualTo(1);

    assertThatThrownBy(() -> habit.apply(new HabitCreatedEvent(aggregateId, 42, "name", "goal")))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("event version mismatch. Expected 2, got 42");
  }

  @Test
  void applyFailsWhenEventIdDoesNotMatchAggregateId() {
    Habit habit = Habit.create(UUID.randomUUID(), "name", "goal");

    assertThatThrownBy(
            () ->
                habit.apply(
                    new HabitCompletedEvent(
                        UUID.randomUUID(), 2, LocalDateTime.now(ZoneId.of("UTC")))))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("aggregate id mismatch");
  }
}
