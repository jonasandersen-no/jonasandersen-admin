package no.jonasandersen.admin.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping("/")
  String index(Model model) {
    model.addAttribute("attribute", "value");

    return "index";
  }

}
