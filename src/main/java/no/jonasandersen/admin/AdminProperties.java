package no.jonasandersen.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jonasandersen.admin")
@Validated
public record AdminProperties(@Valid Minecraft minecraft, @Valid Cloudflare cloudflare,
                              Linode linode, @NotNull String defaultTheme) {

  @Valid
  public record Minecraft(@NotNull String username, @NotNull String password,
                          @NotNull @DefaultValue("22") Integer port) {

  }

  @Valid
  public record Cloudflare(@NotNull String apiKey, @NotNull String zoneId,
                           @NotNull String dnsRecordId) {

  }

  @Valid
  public record Linode(@NotNull String baseUrl, @NotNull String token,
                       @NotNull String rootPassword, @NotNull Long volumeId) {

  }
}
