package no.jonasandersen.admin.core.theme;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.theme.UserSettingsDbo;
import no.jonasandersen.admin.core.theme.domain.Theme;
import no.jonasandersen.admin.core.theme.domain.Username;
import org.junit.jupiter.api.Test;

class UserSettingsServiceTest {


  @Test
  void defaultThemeWhenUnknownUser() {
    UserSettingsService service = UserSettingsService.createNull();

    Theme theme = service.getTheme(new Username("unknown"));

    assertThat(theme).isEqualTo(UserSettingsService.DEFAULT_THEME);
  }

  @Test
  void rightThemeWhenUserIsKnown() {
    UserSettingsService service = UserSettingsService.createNull(new UserSettingsDbo("known", "dark"));

    Theme theme = service.getTheme(new Username("known"));

    assertThat(theme).isEqualTo(new Theme("dark"));
  }

  @Test
  void setThemeUpdatesThemeForUser() {
    UserSettingsService service = UserSettingsService.createNull(new UserSettingsDbo("known", "dark"));

    service.setTheme(new Username("known"), new Theme("light"));

    Theme theme = service.getTheme(new Username("known"));

    assertThat(theme).isEqualTo(new Theme("light"));
  }
}