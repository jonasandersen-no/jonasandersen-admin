package no.jonasandersen.admin.adapter.in.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import no.jonasandersen.admin.domain.ServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server-generator")
public class ServerGeneratorController {

  private static final Logger log = LoggerFactory.getLogger(ServerGeneratorController.class);
  private final ServerGenerator serverGenerator;

  public ServerGeneratorController(ServerGenerator serverGenerator) {
    this.serverGenerator = serverGenerator;
  }

  @Validated
  public record Request(@NotNull ServerType serverType) {

  }

  @Validated
  public record Response(Long id, String label, @NotNull String ip) {

  }

  @PostMapping
  @ApiResponse(responseCode = "200", description = "Server instance created", content = @Content(schema = @Schema(implementation = Response.class)))
  @ApiResponse(responseCode = "500", description = "Failed to create server instance", content = @Content)
  public ResponseEntity<Response> createLinode(@RequestBody Request request) {
    try {
      ServerGeneratorResponse generate = serverGenerator.generate(UsernameResolver.getUsername(), request.serverType);
      return ResponseEntity.ok(new Response(generate.linodeId(), generate.label(), generate.ip().value()));
    } catch (Exception e) {
      log.error("Failed to create linode", e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
