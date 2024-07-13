package no.jonasandersen.admin.domain;

import java.time.LocalDate;
import java.util.List;

public class MultipleMenuItemOnDateException extends RuntimeException {

  public MultipleMenuItemOnDateException(LocalDate date, List<String> items) {
    super("Multiple menu items on date: " + date + " " + items);
  }
}
