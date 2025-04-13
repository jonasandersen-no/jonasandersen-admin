package no.jonasandersen.admin.infrastructure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;
import no.jonasandersen.admin.domain.Feature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jonasandersen.admin")
@Validated
public record AdminProperties(
    @Valid Minecraft minecraft,
    @Valid Cloudflare cloudflare,
    @Valid Linode linode,
    @NotNull String defaultTheme,
    Map<Feature, Boolean> features,
    @Valid Actuator actuator,
    @Valid Discord discord) {

  public AdminProperties {

    if (features == null) {
      features = new EnumMap<>(Feature.class);
    }
  }

  @Valid
  public record Minecraft(
      @NotNull String username,
      @NotNull String password,
      @NotNull @DefaultValue("22") Integer port) {}

  @Valid
  public record Cloudflare(
      @NotNull String apiKey, @NotNull String zoneId, @NotNull String dnsRecordId) {}

  @Valid
  public record Linode(
      @NotNull String baseUrl,
      @NotNull String token,
      @NotNull String rootPassword,
      @NotNull Long volumeId) {}

  @Valid
  public record Actuator(@NotNull String username, @NotNull String password) {}

  @Valid
  public record Discord(@NotNull String token) {}
}
