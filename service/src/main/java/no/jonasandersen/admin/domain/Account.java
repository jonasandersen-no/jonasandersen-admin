package no.jonasandersen.admin.domain;

import java.util.List;

public class Account extends EventSourcedAggregate<AccountEvent, AccountId> {

  private AccountId id;
  private Long balance;
  private String accountName;

  private Account() {}

  public static Account create(AccountId accountId, String name) {
    Account account = new Account();

    account.enqueue(new AccountCreatedEvent(accountId, name));
    return account;
  }

  public void expense(Long amount, String description) {

    enqueue(new ExpenseLoggedEvent(this.id));
  }

  @Override
  protected void apply(AccountEvent event) {
    switch (event) {
      case AccountCreatedEvent(AccountId aggregateId, String name) -> {
        this.id = aggregateId;
        this.accountName = name;
        this.balance = 0L;
      }
      case ExpenseLoggedEvent expenseLoggedEvent -> {
      }
    }
  }

  public static Account reconstitute(List<AccountEvent> accountEvents) {
    Account account = new Account();

    for (AccountEvent event : accountEvents) {
      account.apply(event);
    }

    return account;
  }

  @Override
  public AccountId getId() {
    return id;
  }

  @Override
  public void setId(AccountId id) {
    this.id = id;
  }

  public Long getBalance() {
    return balance;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }
}
