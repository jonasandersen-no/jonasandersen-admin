package no.jonasandersen.admin.adapter.in.web;

import java.util.List;

public record RedirectResponse(List<String> messages) {


  public static RedirectResponse of(String message) {
    return new RedirectResponse(List.of(message));
  }

  public static RedirectResponse of(List<String> messages) {
    return new RedirectResponse(List.copyOf(messages));
  }
}
