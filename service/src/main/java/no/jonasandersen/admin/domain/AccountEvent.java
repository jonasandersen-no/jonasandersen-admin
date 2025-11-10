package no.jonasandersen.admin.domain;

public sealed interface AccountEvent extends Event permits AccountCreatedEvent, ExpenseLoggedEvent {
  AccountId aggregateId();
}
