package no.jonasandersen.admin.adapter.in.api;

import no.jonasandersen.admin.application.ServerGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server-generator")
public class ServerGeneratorController {

  private final ServerGenerator serverGenerator;

  public ServerGeneratorController(ServerGenerator serverGenerator) {
    this.serverGenerator = serverGenerator;
  }

  @PostMapping
  public void createLinode() {
    serverGenerator.generate(ServerGenerator.ServerType.MINECRAFT);
  }
}
