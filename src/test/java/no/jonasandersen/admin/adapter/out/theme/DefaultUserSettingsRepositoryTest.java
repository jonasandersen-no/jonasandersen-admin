package no.jonasandersen.admin.adapter.out.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class DefaultUserSettingsRepositoryTest extends IoBasedTest {

  private static final Logger log = LoggerFactory.getLogger(
      DefaultUserSettingsRepositoryTest.class);
  @Autowired
  private DefaultUserSettingsRepository repository;
  @Autowired
  private CrudUserSettingsRepository jdbcRepository;


  @Test
  void findTheme() {
    UserSettingsDbo saved = jdbcRepository.save(new UserSettingsDbo("findThemeUsername", "theme"));

    log.info("Saved: {}", saved);
    Optional<Theme> theme = repository.findTheme(new Username(saved.getUsername()));

    log.info("Theme: {}", theme);
    assertThat(theme).isNotEmpty()
        .get()
        .extracting(Theme::value)
        .isEqualTo("theme");
  }
}