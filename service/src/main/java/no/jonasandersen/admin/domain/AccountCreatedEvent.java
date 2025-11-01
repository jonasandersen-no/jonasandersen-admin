package no.jonasandersen.admin.domain;

public record AccountCreatedEvent(AccountId aggregateId, String name) implements AccountEvent {}
