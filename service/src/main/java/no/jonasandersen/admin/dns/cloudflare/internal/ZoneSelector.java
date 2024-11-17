package no.jonasandersen.admin.dns.cloudflare.internal;

import java.util.function.Function;
import no.jonasandersen.admin.dns.api.Domain;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.springframework.stereotype.Component;

@Component
public class ZoneSelector implements Function<Domain, String> {

  private final AdminProperties properties;

  public ZoneSelector(AdminProperties properties) {
    this.properties = properties;
  }

  @Override
  public String apply(Domain domain) {
    return switch (domain) {
      case BJOGGIS_COM -> properties.cloudflareBjoggis().zoneId();
      case JONASANDERSEN_NO -> properties.cloudflareJonasandersen().zoneId();
    };
  }
}
