package no.jonasandersen.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping
  public String index(Model model) {

    model.addAttribute("serverString", "Hello from the server!");
    return "front";
  }

}
