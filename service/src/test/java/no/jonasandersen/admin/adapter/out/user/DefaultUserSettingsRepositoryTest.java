package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import no.jonasandersen.admin.application.port.UserSettingsRepository;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class DefaultUserSettingsRepositoryTest extends IoBasedTest {

  private static final Logger log = LoggerFactory.getLogger(
      DefaultUserSettingsRepositoryTest.class);
  @Autowired
  private UserSettingsRepository repository;
  @Autowired
  private CrudUserSettingsRepository jdbcRepository;
  @Autowired
  private CrudUserDboRepository userRepository;

  @Test
  @Transactional
  void findTheme() {
    UserDbo user = userRepository.save(new UserDbo(Username.create("findThemeUsername")));

    UserSettingsDbo saved = jdbcRepository.save(new UserSettingsDbo(user, "theme"));

    log.info("Saved: {}", saved);
    Optional<Theme> theme = repository.findTheme(new Username(saved.getUser().getUsername()));

    log.info("Theme: {}", theme);
    assertThat(theme).isNotEmpty()
        .get()
        .extracting(Theme::value)
        .isEqualTo("theme");
  }
}