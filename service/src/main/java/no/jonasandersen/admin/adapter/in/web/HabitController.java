package no.jonasandersen.admin.adapter.in.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.application.port.Habits;
import no.jonasandersen.admin.domain.Habit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/habits")
public class HabitController {

  private final Habits habits;

  public HabitController(Habits habits) {
    this.habits = habits;
    
    // Create some sample habits for demonstration
    habits.create("Drink water", "Drink 8 glasses of water daily");
    habits.create("Exercise", "Exercise for 30 minutes daily");
    habits.create("Read", "Read for 30 minutes daily");
  }

  @GetMapping
  String listAllHabits(Model model) {
    List<Habit> habitList = habits.findAll();
    model.addAttribute("habits", habitList);
    return "habits/index";
  }

  @GetMapping("/create")
  String showCreateForm() {
    return "habits/create";
  }

  @PostMapping("/create")
  String createHabit(@RequestParam("name") String name, @RequestParam("goal") String goal) {
    habits.create(name, goal);
    return "redirect:/habits";
  }

  @PostMapping("/{habitId}/complete")
  String completeHabit(@PathVariable("habitId") UUID habitId) {
    habits.complete(habitId, LocalDateTime.now());
    return "redirect:/habits";
  }
}