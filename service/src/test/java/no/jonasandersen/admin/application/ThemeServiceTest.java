package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.theme.UserSettingsDbo;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.junit.jupiter.api.Test;

class ThemeServiceTest {


  @Test
  void defaultThemeWhenUnknownUser() {
    ThemeService service = ThemeService.createNull();

    Theme theme = service.findTheme(new Username("unknown"));

    assertThat(theme).isEqualTo(ThemeService.DEFAULT_THEME);
  }

  @Test
  void rightThemeWhenUserIsKnown() {
    ThemeService service = ThemeService.createNull(new UserSettingsDbo("known", "dark"));

    Theme theme = service.findTheme(new Username("known"));

    assertThat(theme).isEqualTo(new Theme("dark"));
  }

  @Test
  void saveThemeUpdatesThemeForUser() {
    ThemeService service = ThemeService.createNull(new UserSettingsDbo("known", "dark"));

    service.saveTheme(Username.create("known"), new Theme("light"));

    Theme theme = service.findTheme(new Username("known"));

    assertThat(theme).isEqualTo(new Theme("light"));
  }
}