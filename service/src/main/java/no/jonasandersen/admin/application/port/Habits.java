package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.Habit;
import java.time.LocalDateTime;
import java.util.UUID;

public interface Habits {

  Habit create(String name, String goal);

  List<Habit> findAll();

  void complete(UUID habitId, LocalDateTime completionTime);
}