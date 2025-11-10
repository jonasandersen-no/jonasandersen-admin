package no.jonasandersen.admin.domain;

public record ExpenseLoggedEvent(AccountId aggregateId, Long amount, String description) implements AccountEvent {

}
