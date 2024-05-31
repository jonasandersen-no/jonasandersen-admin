package no.jonasandersen.admin.adapter.in.api;

import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.ServerType;
import org.springframework.security.core.context.SecurityContextHolder;
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
    String username = getUsername();
    serverGenerator.generate(username, ServerType.MINECRAFT);
  }

  private static String getUsername() {
    String result = "unknown";
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      result = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    return result;
  }
}
