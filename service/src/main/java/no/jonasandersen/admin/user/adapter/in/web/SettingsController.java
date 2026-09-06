package no.jonasandersen.admin.user.adapter.in.web;

import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.user.adapter.UsernameResolver;
import no.jonasandersen.admin.user.application.ThemeService;
import no.jonasandersen.admin.user.domain.Username;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class SettingsController {

  public static final String REDIRECT_SETTINGS = "redirect:/settings";
  private final ThemeService themeService;

  public SettingsController(ThemeService themeService) {
    this.themeService = themeService;
  }

  @GetMapping
  String settings(Model model) {
    model.addAttribute(
        "currentTheme",
        themeService.findTheme(Username.create(UsernameResolver.getUsernameAsString())));

    return "settings/index";
  }

  @PostMapping
  String saveSettings(@RequestParam String theme) {
    String userName = UsernameResolver.getUsernameAsString();

    themeService.saveTheme(Username.create(userName), Theme.from(theme));

    return REDIRECT_SETTINGS;
  }
}
