package no.jonasandersen.admin.domain;

public sealed interface AccountEvent extends Event permits AccountCreatedEvent {
  AccountId aggregateId();
}
