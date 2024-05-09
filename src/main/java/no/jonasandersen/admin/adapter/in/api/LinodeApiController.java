package no.jonasandersen.admin.adapter.in.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/linode")
public class LinodeApiController {

  private LinodeApiController() {
  }

  @PostMapping
  public void createLinode() {

  }
}
