package no.jonasandersen.admin.domain;

import java.time.LocalDate;

public class NoItemOnDateException extends RuntimeException {

  public NoItemOnDateException(LocalDate date) {
    super("No menu item on date: " + date);
  }
}
