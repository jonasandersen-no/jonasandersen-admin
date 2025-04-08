package no.jonasandersen.admin.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record MenuItem(LocalDate date, String description) {

  public static final String DATE_PATTERN = "EEEE d. MMMM";
  public static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern(DATE_PATTERN).withLocale(Locale.ENGLISH);

  public String formattedDate() {
    return date.format(FORMATTER);
  }
}
