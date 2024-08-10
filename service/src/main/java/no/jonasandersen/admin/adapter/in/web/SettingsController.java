package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.application.AccessControl;
import no.jonasandersen.admin.application.ThemeService;
import no.jonasandersen.admin.domain.Theme;
import no.jonasandersen.admin.domain.User;
import no.jonasandersen.admin.domain.Username;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class SettingsController {

  private final ThemeService themeService;
  private final AccessControl accessControl;

  public SettingsController(ThemeService themeService, AccessControl accessControl) {
    this.themeService = themeService;
    this.accessControl = accessControl;
  }

  @GetMapping
  String settings(Model model) {
    model.addAttribute("currentTheme",
        themeService.findTheme(Username.create(UsernameResolver.getUsernameAsString())));

    List<User> allowedUsers = accessControl.getAllowedUsers();
    model.addAttribute("allowedUsers", allowedUsers);
    return "settings/index";
  }

  @PostMapping
  String saveSettings(@RequestParam String theme) {
    String userName = UsernameResolver.getUsernameAsString();

    themeService.saveTheme(Username.create(userName), Theme.from(theme));

    return "redirect:/settings";
  }

  @PostMapping("/allow-user")
  @Secured("ROLE_ADMIN")
  String addUserToAccessControl(@RequestParam String email) {
    accessControl.allowUser(email);
    return "redirect:/settings";
  }

  @DeleteMapping("/revoke-user")
  @Secured("ROLE_ADMIN")
  String removeUserFromAccessControl(@RequestParam String email) {
    accessControl.revokeUser(email);
    return "redirect:/settings";
  }

}
