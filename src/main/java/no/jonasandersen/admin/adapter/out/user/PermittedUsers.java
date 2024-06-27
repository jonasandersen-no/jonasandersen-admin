package no.jonasandersen.admin.adapter.out.user;

import java.util.concurrent.TimeUnit;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.infrastructure.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PermittedUsers {

  private final Logger log = LoggerFactory.getLogger(PermittedUsers.class);
  private final JdbcClient jdbcClient;

  public PermittedUsers(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Cacheable("permittedUsers")
  public boolean isAllowed(String subject, String email) {
    log.info("Checking if user with subject {} and email {} is permitted", subject, email);
    if (Features.isEnabled(Feature.ALLOW_ALL_USERS)) {
      return true;
    }

    Integer count = jdbcClient.sql(
            "select count(*) from permitted_users where subject = ? and email = ?")
        .params(subject, email)
        .query(Integer.class)
        .single();

    return count == 1;
  }

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
  @CacheEvict(value = "permittedUsers", allEntries = true)
  public void clearCache() {
  }

}
