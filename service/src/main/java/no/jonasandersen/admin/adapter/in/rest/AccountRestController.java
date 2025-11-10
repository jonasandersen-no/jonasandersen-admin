package no.jonasandersen.admin.adapter.in.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.jonasandersen.admin.application.AccountBalanceProjection;
import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.port.AccountStreamRepository;
import no.jonasandersen.admin.domain.Account;
import no.jonasandersen.admin.domain.AccountEvent;
import no.jonasandersen.admin.domain.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {

  private static final Logger log = LoggerFactory.getLogger(AccountRestController.class);
  private final AccountBalanceProjection accountBalanceProjection;
  private final EventStore<AccountId, AccountEvent, Account> eventStore;
  private final AccountStreamRepository repository;

  public AccountRestController(
      AccountBalanceProjection accountBalanceProjection,
      EventStore<AccountId, AccountEvent, Account> eventStore,
      AccountStreamRepository repository) {
    this.accountBalanceProjection = accountBalanceProjection;
    this.eventStore = eventStore;
    this.repository = repository;
  }

  record CreateAccountRequest(@Parameter(description = "Account Name") String name) {}

  record CreateAccountResponse(UUID accountId) {}

  UUID myId = UUID.fromString("595cf6b7-73b9-400e-a232-3a41b853c442");

  @GetMapping
  List<CreateAccountResponse> getAllAccounts() {
    List<AccountId> accounts = repository.getAccountsForUser(myId);

    return accounts.stream().map(accountId -> new CreateAccountResponse(accountId.id())).toList();
  }

  @GetMapping("/{accountId}/balance")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Account balance"),
        @ApiResponse(responseCode = "400", description = "Account does not exist")
      })
  Long accountBalance(@PathVariable UUID accountId) {
    Long balance = accountBalanceProjection.forAccount(new AccountId(accountId));
    if (balance == null) {
      throw new IllegalArgumentException("No account with id " + accountId);
    }
    return balance;
  }

  record LogExpenseRequest(Long amount, String description) {}

  @PostMapping("/{accountId}/expense")
  void expense(@RequestBody LogExpenseRequest request, @PathVariable String accountId) {
    Optional<Account> account = eventStore.findById(AccountId.of(accountId));

    account.ifPresent(
        account1 -> {
          account1.expense(request.amount(), request.description());

          eventStore.save(account1);
        });
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Account created"),
        @ApiResponse(responseCode = "400", description = "Bad request")
      })
  CreateAccountResponse create(@RequestBody CreateAccountRequest request) {
    log.info("Received request: {}", request);
    AccountId accountId = AccountId.random();

    Account account = Account.create(accountId, request.name());
    eventStore.save(account);

    CreateAccountResponse response = new CreateAccountResponse(accountId.id());

    repository.save(myId, accountId);
    return response;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }
}
