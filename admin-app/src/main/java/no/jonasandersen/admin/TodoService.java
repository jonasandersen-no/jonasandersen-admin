package no.jonasandersen.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private final List<TodoItem> items;

  private final AtomicInteger idSequence = new AtomicInteger(0);

  public TodoService() {
    items = new ArrayList<>(Instancio.ofList(TodoItem.class)
        .size(5)
        .supply(Select.field(TodoItem::id), (Supplier<Object>) idSequence::getAndIncrement)
        .create());
  }

  public TodoItem createTodoItem(String title, String description) {
    TodoItem item = new TodoItem(idSequence.getAndIncrement(), title, description);
    items.add(item);
    return item;
  }

  public List<TodoItem> getTodoItems() {
    return items;
  }

  public void deleteTodoItem(Integer id) {
    items.removeIf(item -> item.id().equals(id));
  }


  public record TodoItem(Integer id, String title, String description) {

  }
}


