package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountTest {

  @Nested
  class CommandsGenerateEvents {

    @Test
    void createGeneratesAccountCreatedEvent() {
      UUID uuid = UUID.randomUUID();

      Account account = Account.create(new AccountId(uuid), "myAccount");

      assertThat(account.uncommittedEvents())
          .containsExactly(new AccountCreatedEvent(new AccountId(uuid), "myAccount"));
    }

    @Test
    void logExpenseGeneratesExpenseLoggedEvent() {
      AccountId accountId = AccountId.random();
      String name = "myAccount";

      Account account =
          Account.reconstitute(List.of(new AccountCreatedEvent(accountId, name)));

      account.expense(1000L, "Water");

      assertThat(account.uncommittedEvents())
          .containsExactly(new ExpenseLoggedEvent(accountId, 1000L, "Water"));
    }

  }

  @Nested
  class EventsGenerateState {

    @Test
    void accountCreatedEventUpdatesIdAndName() {
      UUID aggregateId = UUID.randomUUID();
      String name = "myAccount";

      Account account =
          Account.reconstitute(List.of(new AccountCreatedEvent(new AccountId(aggregateId), name)));

      assertThat(account.getId()).isEqualTo(new AccountId(aggregateId));
      assertThat(account.getAccountName()).isEqualTo(name);
      assertThat(account.getBalance()).isNotNull();
    }

    @Test
    void expenseLoggedEventsSubtractsBalance() {
      AccountId random = AccountId.random();

      Account account = Account.reconstitute(List.of(new ExpenseLoggedEvent(random, 10L, "")));

      assertThat(account.getBalance()).isEqualTo(-10L);
    }
  }
}
