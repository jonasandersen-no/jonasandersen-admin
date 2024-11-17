package no.jonasandersen.admin.dns.cloudflare.internal;

import no.jonasandersen.admin.dns.cloudflare.api.DnsApi;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudflareDnsConfiguration {

  @Bean
  DnsApi dnsApi(AdminProperties properties, ZoneSelector zoneSelector) {
    return new CloudflareApi(properties, zoneSelector);
  }
}
