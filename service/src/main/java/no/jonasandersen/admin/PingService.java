package no.jonasandersen.admin;

import java.time.OffsetDateTime;
import java.util.List;
import no.jonasandersen.admin.generated.api.PingApi;
import no.jonasandersen.admin.generated.model.PingResponse;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class PingService implements PingApi {

  private final BuildProperties buildProperties;
  private final Environment environment;

  public PingService(BuildProperties buildProperties, Environment environment) {
    this.buildProperties = buildProperties;
    this.environment = environment;
  }

  @Override
  public ResponseEntity<PingResponse> getPing() {
    PingResponse response =
        new PingResponse()
            .name(buildProperties.getName())
            .version(buildProperties.getVersion())
            .buildTime(String.valueOf(buildProperties.getTime()))
            .activeProfiles(List.of(environment.getActiveProfiles()))
            .serverTime(OffsetDateTime.now().toString());
    return ResponseEntity.ok(response);
  }
}
