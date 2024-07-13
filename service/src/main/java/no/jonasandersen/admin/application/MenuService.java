package no.jonasandersen.admin.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import no.jonasandersen.admin.domain.MenuItem;
import no.jonasandersen.admin.domain.MultipleMenuItemOnDateException;
import no.jonasandersen.admin.domain.NoItemOnDateException;
import org.springframework.util.MultiValueMap;

public class MenuService {

  private List<MenuItem> menuItems;

  public MenuService() {
    LocalDate now = LocalDate.now();
    LocalDate tomorrow = now.plusDays(1);

    menuItems = List.of(
        new MenuItem(now, "Pasta"),
        new MenuItem(tomorrow, "Pizza")
    );
  }

  public List<MenuItem> listMenu() {
    return menuItems;
  }

  public void update(MultiValueMap<String, String> data) {
    List<MenuItem> newItems = new ArrayList<>();
    for (Entry<String, List<String>> entry : data.entrySet()) {
      LocalDate date = LocalDate.parse(entry.getKey());

      validateDescription(entry, date);

      String description = entry.getValue().getFirst();

      newItems.add(new MenuItem(date, description));
    }

    menuItems = List.copyOf(newItems);
  }

  private static void validateDescription(Entry<String, List<String>> entry, LocalDate date) {
    if (entry.getValue().size() > 1) {
      throw new MultipleMenuItemOnDateException(date, entry.getValue());
    } else if (entry.getValue().isEmpty()) {
      throw new NoItemOnDateException(date);
    }
  }

  public static MenuService configureForTest() {
    return configureForTest(UnaryOperator.identity());
  }


  public static MenuService configureForTest(UnaryOperator<Config> configure) {
    Config config = configure.apply(new Config());
    MenuService menuService = new MenuService();

    menuService.setMenuItems(config.menuItems);
    return menuService;
  }

  private void setMenuItems(List<MenuItem> menuItems) {
    this.menuItems = new ArrayList<>(menuItems);
  }

  public static class Config {

    List<MenuItem> menuItems = Collections.emptyList();

    public Config add(MenuItem... menuItems) {
      this.menuItems = List.of(menuItems);
      return this;
    }
  }
}
