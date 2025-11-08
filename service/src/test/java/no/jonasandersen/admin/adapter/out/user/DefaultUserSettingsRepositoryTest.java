package no.jonasandersen.admin.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class DefaultUserSettingsRepositoryTest extends IoBasedTest {

  private static final Logger log =
      LoggerFactory.getLogger(DefaultUserSettingsRepositoryTest.class);
  @Autowired private CrudUserDboRepository userRepository;


  @Test
  void findTheme() {
    UserDbo user = userRepository.save(new UserDbo(Username.create("findThemeUsername")));

    UserSettingsDbo dbo = new UserSettingsDbo("theme");

    user.setSettings(dbo);
    userRepository.save(user);

    log.info("Saved: {}", user);
    Optional<Theme> theme =
        Optional.ofNullable(user.getSettings()).map(UserSettingsDbo::getTheme).map(Theme::new);

    log.info("Theme: {}", theme);
    assertThat(theme).isNotEmpty().get().extracting(Theme::value).isEqualTo("theme");
  }
}
