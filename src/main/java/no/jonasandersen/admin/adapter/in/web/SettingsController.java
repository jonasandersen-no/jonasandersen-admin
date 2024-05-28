package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.adapter.DefaultPrincipalNameResolver;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.Username;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class SettingsController {

  private final ThemeService themeService;
  private final DefaultPrincipalNameResolver principalNameResolver = DefaultPrincipalNameResolver.create();

  public SettingsController(ThemeService themeService) {
    this.themeService = themeService;
  }

  @GetMapping
  String settings(Model model) {
    model.addAttribute("currentTheme",
        themeService.findTheme(Username.create(principalNameResolver.get())));
    return "settings/index";
  }

  @PostMapping
  public String saveSettings(@RequestParam String theme) {
    String userName = principalNameResolver.get();

    themeService.saveTheme(Username.create(userName), Theme.from(theme));

    return "redirect:/settings";
  }

}
