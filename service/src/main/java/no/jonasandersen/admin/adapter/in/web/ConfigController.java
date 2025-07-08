package no.jonasandersen.admin.adapter.in.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/config")
@Secured("ROLE_ADMIN")
public class ConfigController {

  @GetMapping
  String index() {
    return "config/index";
  }
}
