package no.jonasandersen.admin.domain;

import java.math.BigDecimal;
import java.util.List;

public class Account extends EventSourcedAggregate<AccountEvent, AccountId> {

  private AccountId id;
  private BigDecimal balance;
  private String accountName;

  @Override
  protected void apply(AccountEvent event) {}

  public static Account reconstitute(List<AccountEvent> accountEvents) {
    Account account = new Account();

    for (AccountEvent event : accountEvents) {
      account.apply(event);
    }

    return account;
  }
}
