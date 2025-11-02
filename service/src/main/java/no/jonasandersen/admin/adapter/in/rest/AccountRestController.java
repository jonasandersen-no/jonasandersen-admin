package no.jonasandersen.admin.adapter.in.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import no.jonasandersen.admin.application.AccountBalanceProjection;
import no.jonasandersen.admin.application.EventStore;
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

  public AccountRestController(
      AccountBalanceProjection accountBalanceProjection,
      EventStore<AccountId, AccountEvent, Account> eventStore) {
    this.accountBalanceProjection = accountBalanceProjection;
    this.eventStore = eventStore;
  }

  record CreateAccountRequest(@Parameter(description = "Account Name") String name) {}

  record CreateAccountResponse(UUID accountId) {}

  @GetMapping("/{accountId}/balance")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Account balance"),
          @ApiResponse(responseCode = "400", description = "Account does not exist")
      }
  )
  Long accountBalance(@PathVariable UUID accountId) {
    Long balance = accountBalanceProjection.forAccount(new AccountId(accountId));
    if (balance == null) {
      throw new IllegalArgumentException("No account with id " + accountId);
    }
    return balance;
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

    return new CreateAccountResponse(accountId.id());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }
}
