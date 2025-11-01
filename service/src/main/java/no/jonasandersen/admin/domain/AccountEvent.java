package no.jonasandersen.admin.domain;

public non-sealed interface AccountEvent extends Event {
  AccountId aggregateId();
}
