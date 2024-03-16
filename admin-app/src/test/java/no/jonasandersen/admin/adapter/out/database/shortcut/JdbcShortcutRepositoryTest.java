package no.jonasandersen.admin.adapter.out.database.shortcut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.ShortcutRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;

class JdbcShortcutRepositoryTest extends IoBasedTest {

  @Autowired
  private JdbcClient jdbcClient;

  @Autowired
  private ShortcutRepository shortcutRepository;

  @Nested
  class Save {

    @Test
    void storesShortcutInDatabase() {
      shortcutRepository.save(new Shortcut(null, "Save", "Ctrl+Shift+T", "Open new tab"));

      assertThat(jdbcClient.sql("""
              select count(id) from shortcut \
              where project = ? and shortcut = ? and description = ?
              """)
          .params("Save", "Ctrl+Shift+T", "Open new tab")
          .query(Integer.class)
          .single()).isEqualTo(1);
    }

    @Test
    void storeSameShortcutTwiceThrowsDuplicateKeyException() {
      shortcutRepository.save(new Shortcut(null, "Duplicate", "Ctrl+Shift+T", "Open new tab"));

      assertThatThrownBy(() -> shortcutRepository
          .save(new Shortcut(null, "Duplicate", "Ctrl+Shift+T", "Open new tab")))
          .isInstanceOf(DuplicateKeyException.class);
    }
  }

  @Nested
  class Find {

    @Test
    void findAllReturnsAllShortcuts() {
      // Clear all previous shortcuts
      jdbcClient.sql("delete from shortcut").update();

      shortcutRepository.save(new Shortcut(null, "FindAll", "Ctrl+Shift+T", "Open new tab"));
      shortcutRepository.save(new Shortcut(null, "FindAll", "Ctrl+Shift+N", "Open new window"));
      shortcutRepository.save(new Shortcut(null, "FindAll", "Ctrl+Shift+W", "Close window"));

      assertThat(shortcutRepository.findAll()).hasSize(3);
    }

    @Test
    void findByProjectReturnsShortcutsForProject() {
      shortcutRepository
          .save(new Shortcut(null, "FindByProject1", "Ctrl+Shift+T", "Open new tab"));
      shortcutRepository
          .save(new Shortcut(null, "FindByProject2", "Ctrl+Shift+N", "Open new window"));
      shortcutRepository
          .save(new Shortcut(null, "FindByProject3", "Ctrl+Shift+W", "Close window"));

      assertThat(shortcutRepository.findByProject("FindByProject1")).hasSize(1);
    }
  }
}