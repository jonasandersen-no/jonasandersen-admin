package no.jonasandersen.admin.adapter.out.measurement;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DeaultCheckDatabaseConnection implements CheckDatabaseConnection {

  private static final Logger log = LoggerFactory.getLogger(DeaultCheckDatabaseConnection.class);
  private final EntityManager entityManager;

  DeaultCheckDatabaseConnection(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public boolean check() {
    try {
      entityManager.createNativeQuery("SELECT 1").getSingleResult();
    } catch (Exception e) {
      log.error("Failed to connect to database, skipping storing measurements");
      return false;
    }
    return true;
  }
}
