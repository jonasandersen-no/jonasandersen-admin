package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.TodoService.TodoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  private final TodoService service;

  public IndexController(TodoService service) {
    this.service = service;
  }

  private final Logger logger = LoggerFactory.getLogger(IndexController.class);

  @GetMapping("/")
  public String index(Model model) {

    List<TodoItem> todoItems = service.getTodoItems();
    model.addAttribute("todoItems", todoItems);
    return "index";
  }

  @GetMapping("/linode")
  String linode() {
    return "linode";
  }

  @GetMapping("/test")
  String test() {
    return "test";
  }
}
