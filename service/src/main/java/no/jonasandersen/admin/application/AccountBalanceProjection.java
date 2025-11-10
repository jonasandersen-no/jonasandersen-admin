package no.jonasandersen.admin.application;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import no.jonasandersen.admin.domain.Account;
import no.jonasandersen.admin.domain.AccountCreatedEvent;
import no.jonasandersen.admin.domain.AccountEvent;
import no.jonasandersen.admin.domain.AccountId;
import no.jonasandersen.admin.domain.ExpenseLoggedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountBalanceProjection {

  private static final Logger log = LoggerFactory.getLogger(AccountBalanceProjection.class);
  private final EventStore<AccountId, AccountEvent, Account> eventStore;

  private final Map<AccountId, Long> balance = new HashMap<>();

  public AccountBalanceProjection(EventStore<AccountId, AccountEvent, Account> eventStore) {
    this.eventStore = eventStore;
  }

  @EventListener
  public void onEvent(AccountEvent event) {
    log.info("Received event {}", event);

    switch (event) {
      case AccountCreatedEvent(AccountId aggregateId, _) -> {
        balance.put(aggregateId, 0L);
      }
      case ExpenseLoggedEvent(AccountId aggregateId, Long amount, _) -> {
        Long l = balance.get(aggregateId);
        l -= amount;
        balance.put(aggregateId, l);
      }
    }
  }

  public Long forAccount(AccountId accountId) {
    log.info("Fetching balance for {}", accountId);
    Long balance = this.balance.get(accountId);
    if (balance == null) {
      regenerate(accountId);
      return this.balance.get(accountId);
    }
    return balance;
  }

  private void regenerate(AccountId accountId) {
    log.info("No cached value for {}, regenerating projection.", accountId);
    Stream<AccountEvent> events = eventStore.allEvents(accountId);
    events.forEachOrdered(this::onEvent);
  }
}
