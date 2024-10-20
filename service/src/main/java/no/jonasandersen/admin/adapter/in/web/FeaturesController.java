package no.jonasandersen.admin.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/features")
public class FeaturesController {

  @GetMapping
  String features() {
    return "features";
  }

}
