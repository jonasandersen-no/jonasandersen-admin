package no.jonasandersen.admin.application.port;

import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.domain.AccountId;

public interface AccountStreamRepository {

  List<AccountId> getAccountsForUser(UUID userId);

  void save(UUID myId, AccountId accountId);
}
