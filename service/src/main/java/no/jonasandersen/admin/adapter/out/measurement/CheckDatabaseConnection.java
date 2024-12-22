package no.jonasandersen.admin.adapter.out.measurement;

import jakarta.persistence.EntityManager;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckDatabaseConnection implements Supplier<Boolean> {

  private static final Logger log = LoggerFactory.getLogger(CheckDatabaseConnection.class);
  private final EntityManager entityManager;

  public CheckDatabaseConnection(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Boolean get() {
    try {
      entityManager.createNativeQuery("SELECT 1").getSingleResult();
    } catch (Exception e) {
      log.error("Failed to connect to database, skipping storing measurements");
      return false;
    }
    return true;
  }
}
