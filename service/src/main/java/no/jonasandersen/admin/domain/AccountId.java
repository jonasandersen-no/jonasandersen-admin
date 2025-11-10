package no.jonasandersen.admin.domain;

import java.util.UUID;

public record AccountId(UUID id) implements Id {

  public static AccountId random() {
    return new AccountId(UUID.randomUUID());
  }

  public static AccountId of(UUID aggregateId) {
    return new AccountId(aggregateId);
  }

  public static AccountId of(String accountId) {
    return new AccountId(UUID.fromString(accountId));
  }
}
