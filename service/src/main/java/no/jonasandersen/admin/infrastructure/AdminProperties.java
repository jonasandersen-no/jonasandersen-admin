package no.jonasandersen.admin.infrastructure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;
import no.jonasandersen.admin.domain.Feature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jonasandersen.admin")
@Validated
public record AdminProperties(
    Map<Feature, Boolean> features,
    @Valid Actuator actuator,
    @Valid Discord discord,
    @Valid Minecraft minecraft) {

  public AdminProperties {

    if (features == null) {
      features = new EnumMap<>(Feature.class);
    }
  }

  @Valid
  public record Actuator(@NotNull String username, @NotNull String password) {}

  @Valid
  public record Discord(@NotNull String token) {}

  @Valid
  public record Minecraft(Andersen andersen) {

    public record Andersen(String username, String password, String hostname) {}
  }
}
