package no.jonasandersen.admin.domain;

public record ExpenseLoggedEvent(AccountId aggregateId) implements AccountEvent {

}
