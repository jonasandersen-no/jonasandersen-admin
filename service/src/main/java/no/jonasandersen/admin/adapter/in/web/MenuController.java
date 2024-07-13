package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.application.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {


  private final MenuService menuService;

  public MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping
  String menu(Model model) {

    model.addAttribute("menuItems", menuService.listMenu());
    return "menu/index";
  }

}
