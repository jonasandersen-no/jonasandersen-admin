package no.jonasandersen.admin.adapter.out.user;

import java.util.concurrent.TimeUnit;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.infrastructure.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PermittedUsers {

  private final Logger log = LoggerFactory.getLogger(PermittedUsers.class);
  private final CrudPermittedUserRepository repository;

  public PermittedUsers(CrudPermittedUserRepository repository) {
    this.repository = repository;
  }

  @Cacheable("permittedUsers")
  public boolean isAllowed(String subject, String email) {
    log.info("Checking if user with subject {} and email {} is permitted", subject, email);
    if (Features.isEnabled(Feature.ALLOW_ALL_USERS)) {
      return true;
    }

    return repository.existsBySubjectAndEmail(subject, email);
  }

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
  @CacheEvict(value = "permittedUsers", allEntries = true)
  public void clearCache() {
  }

}
