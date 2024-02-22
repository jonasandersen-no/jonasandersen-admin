package no.jonasandersen.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
  private final List<TodoItem> items = new ArrayList<>();

  private final AtomicInteger idSequence = new AtomicInteger(0);

  public TodoService() {
//    items = new ArrayList<>(Instancio.ofList(TodoItem.class)
//        .size(5)
//        .supply(Select.field(TodoItem::id), (Supplier<Object>) idSequence::getAndIncrement)
//        .create());
  }

  public TodoItem createTodoItem(String title, String description) {
    TodoItem item = new TodoItem(idSequence.getAndIncrement(), title, description);
    items.add(item);
    return item;
  }

  public List<TodoItem> getTodoItems() {
    logger.info("Returning items: {}", items);
    return items;
  }

  public void deleteTodoItem(Integer id) {
    items.removeIf(item -> item.id().equals(id));
  }


  public record TodoItem(Integer id, String title, String description) {

  }
}


