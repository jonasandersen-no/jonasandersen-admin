package no.jonasandersen.admin;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import java.util.List;
import no.jonasandersen.admin.TodoService.TodoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

  private final TodoService service;

  private final Logger logger = LoggerFactory.getLogger(TodoController.class);

  public TodoController(TodoService service) {
    this.service = service;
  }

  @PostMapping()
  @HxRequest
  public String create(@ModelAttribute TodoItem item) {
    logger.info("Received item: {}", item);
    TodoItem created = service.createTodoItem(item.title(), item.description());

//    model.addAttribute("item", created);
    return """
        <tr id="list-item-%s">
          <td>%s</td>
          <td>%s</td>
          <td>%s</td>
          <td>
            <button
                hx-delete="/api/todo/%s"
                hx-target="#list-item-%s"
                hx-swap="outerHTML">
                Delete
              </button>
          </td>
        </tr>
        """.formatted(created.id(), created.title(), created.description(), created.id(),
        created.id(),created.id());
  }

  @DeleteMapping("/{id}")
  @HxRequest
  public String delete(@PathVariable Integer id) {
    logger.info("Received delete request for id: {}", id);
    service.deleteTodoItem(id);
    return "";
  }

  @GetMapping
  @HxRequest
  public String getTodoItems() {
    List<TodoItem> todoItems = service.getTodoItems();

    List<String> list = todoItems.stream()
        .map(item -> """
            <tr>
              <td>%s</td>
              <td>%s</td>
            </tr>
            """.formatted(item.title(), item.description()))
        .toList();

    return """
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            %s
          </tbody>
        """.formatted(String.join("", list));

  }

}
