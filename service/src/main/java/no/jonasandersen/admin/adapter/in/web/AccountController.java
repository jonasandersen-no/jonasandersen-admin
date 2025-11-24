package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.adapter.HtmlElement;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

  String accounts() {
    return HtmlElement.div(null, HtmlElement.text("Testing")).render();
  }
}
