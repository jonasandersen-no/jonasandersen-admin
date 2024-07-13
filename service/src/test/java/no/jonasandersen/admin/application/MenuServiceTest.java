package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.List;
import no.jonasandersen.admin.domain.MenuItem;
import no.jonasandersen.admin.domain.MultipleMenuItemOnDateException;
import no.jonasandersen.admin.domain.NoItemOnDateException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class MenuServiceTest {

  @Test
  void updatingMenuStoresData() {
    MenuService service = MenuService.configureForTest();

    MultiValueMap<String, String> data = pasta();

    service.update(data);

    assertThat(service.listMenu()).containsExactly(
        new MenuItem(LocalDate.of(2024, Month.JULY, 10), "Pasta")
    );
  }

  private static @NotNull MultiValueMap<String, String> pasta() {
    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.put("2024-07-10", List.of("Pasta"));
    return data;
  }

  @Test
  void existingDataDeletedWhenUpdatingData() {
    MenuService service = MenuService.configureForTest(config ->
        config.add(new MenuItem(LocalDate.of(1, Month.JANUARY, 1), "Some Food")));

    assertThat(service.listMenu())
        .containsExactly(new MenuItem(LocalDate.of(1, Month.JANUARY, 1), "Some Food"));

    service.update(pasta());

    assertThat(service.listMenu())
        .containsExactly(new MenuItem(LocalDate.of(2024, Month.JULY, 10), "Pasta"));
  }

  @Test
  void dataKeyShouldBeDate() {

    MenuService service = MenuService.configureForTest();
    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.put("THISISNOTADATE", List.of("Pasta"));

    assertThatThrownBy(() -> service.update(data))
        .isInstanceOf(DateTimeParseException.class)
        .hasMessage("Text 'THISISNOTADATE' could not be parsed at index 0");
  }

  @Test
  void dataValueShouldNotContainMultipleItems() {
    MenuService service = MenuService.configureForTest();
    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.put("2024-07-10", List.of("Pasta", "Two items"));

    assertThatThrownBy(() -> service.update(data))
        .isInstanceOf(MultipleMenuItemOnDateException.class)
        .hasMessage("Multiple menu items on date: 2024-07-10 [Pasta, Two items]");
  }

  @Test
  void dataValueShouldNotBeEmpty() {
    MenuService service = MenuService.configureForTest();
    LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.put("2024-07-10", List.of());
    assertThatThrownBy(() -> service.update(data))
        .isInstanceOf(NoItemOnDateException.class)
        .hasMessage("No menu item on date: 2024-07-10");
  }
}
