package no.jonasandersen.admin.adapter.in.api;

import no.jonasandersen.admin.application.ServerGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/linode")
public class LinodeApiController {

  private final ServerGenerator serverGenerator;

  public LinodeApiController(ServerGenerator serverGenerator) {
    this.serverGenerator = serverGenerator;
  }

  @PostMapping
  public void createLinode() {
    serverGenerator.generate(ServerGenerator.ServerType.MINECRAFT);
  }
}
