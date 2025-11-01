package no.jonasandersen.admin.infrastructure;

import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.port.EventBus;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import no.jonasandersen.admin.domain.Account;
import no.jonasandersen.admin.domain.AccountEvent;
import no.jonasandersen.admin.domain.AccountId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountEventConfiguration {

  @Bean
  EventStore<AccountId, AccountEvent, Account> accountEventStore(
      EventStoreRepository repository, EventBus eventBus) {
    return EventStore.forAccount(repository, eventBus);
  }
}
