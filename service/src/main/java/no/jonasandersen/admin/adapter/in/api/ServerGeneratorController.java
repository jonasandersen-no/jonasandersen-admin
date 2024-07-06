package no.jonasandersen.admin.adapter.in.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.jonasandersen.admin.adapter.UsernameResolver;
import no.jonasandersen.admin.api.CreateServerRequest;
import no.jonasandersen.admin.api.CreateServerResponse;
import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.ServerGeneratorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

  @PostMapping
  @ApiResponse(responseCode = "200", description = "Server instance created", content = @Content(schema = @Schema(implementation = CreateServerResponse.class)))
  @ApiResponse(responseCode = "500", description = "Failed to create server instance", content = @Content)
  public ResponseEntity<CreateServerResponse> createLinode(@RequestBody CreateServerRequest request) {
    try {
      ServerGeneratorResponse generate = serverGenerator.generate(UsernameResolver.getUsername(),
          ServerTypeConverter.convert(request.serverType()));
      return ResponseEntity.ok(new CreateServerResponse(generate.linodeId(), generate.label(), generate.ip().value()));
    } catch (Exception e) {
      log.error("Failed to create linode", e);
      return ResponseEntity.internalServerError().build();
    }
  }

}
