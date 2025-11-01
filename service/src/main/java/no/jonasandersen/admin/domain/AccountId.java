package no.jonasandersen.admin.domain;

import java.util.UUID;

public record AccountId(UUID id) implements Id {

  public static AccountId random() {
    return new AccountId(UUID.randomUUID());
  }
}
