package no.jonasandersen.admin.out.account;

import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.application.port.AccountStreamRepository;
import no.jonasandersen.admin.domain.AccountId;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccountStreamRepository implements AccountStreamRepository {

  private final JdbcTemplate jdbcTemplate;

  public DefaultAccountStreamRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<AccountId> getAccountsForUser(UUID userId) {
    List<@Nullable UUID> uuids = jdbcTemplate.queryForList(
        "select stream_id from public.account_stream where user_id = ?", UUID.class, userId);
    return uuids.stream().map(AccountId::new).toList();
  }

  @Override
  public void save(UUID myId, AccountId accountId) {
    jdbcTemplate.update(
        "insert into account_stream(user_id, stream_id) VALUES (?,?)",
        myId, accountId.id());
  }
}
