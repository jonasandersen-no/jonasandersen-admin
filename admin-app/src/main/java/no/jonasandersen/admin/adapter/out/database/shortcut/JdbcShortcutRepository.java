package no.jonasandersen.admin.adapter.out.database.shortcut;

import java.util.List;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcShortcutRepository implements ShortcutRepository {

  private final JdbcClient jdbcClient;
  private static final Logger logger = LoggerFactory.getLogger(JdbcShortcutRepository.class);

  public JdbcShortcutRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Shortcut save(Shortcut shortcut) {
    Shortcut entity = Shortcut.from(shortcut);
    int update = jdbcClient.sql("""
            insert into shortcut(project, shortcut, description) \
            values(?, ?, ?)""")
        .params(entity.project(), entity.shortcut(), entity.description())
        .update();

    logger.info("Inserted {} rows", update);
    return entity;
  }

  @Override
  public List<Shortcut> findAll() {
    return jdbcClient.sql("select * from shortcut")
        .query(Shortcut.class)
        .list();
  }

  @Override
  public List<Shortcut> findByProject(String project) {
    return jdbcClient.sql("select * from shortcut where project = ?")
        .params(project)
        .query(Shortcut.class)
        .list();
  }

  @Override
  public void update(Shortcut shortcut) {
    int update = jdbcClient.sql("""
            update shortcut \
            set project = ?, shortcut = ?, description = ? \
            where id = ?
            """)
        .params(shortcut.project(), shortcut.shortcut(), shortcut.description(), shortcut.id())
        .update();

    logger.info("Updated {} rows", update);
  }

  @Override
  public void delete(Long id) {
    int update = jdbcClient.sql("delete from shortcut where id = ?")
        .params(id)
        .update();

    logger.info("Deleted {} rows", update);
  }
}
