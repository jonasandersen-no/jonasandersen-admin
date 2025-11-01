package no.jonasandersen.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountTest {

  @Nested
  class CommandsGenerateEvents {

    @Test
    void createAccountCommandGeneratesIdAndName() {
      UUID uuid = UUID.randomUUID();

      Account account = Account.create(new AccountId(uuid), "myAccount");

      assertThat(account.uncommittedEvents())
          .containsExactly(new AccountCreatedEvent(new AccountId(uuid), "myAccount"));
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
  }
}
