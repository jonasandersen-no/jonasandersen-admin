package no.jonasandersen.admin.dns.internal;

import no.jonasandersen.admin.dns.api.DnsManager;
import no.jonasandersen.admin.dns.cloudflare.api.DnsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DnsConfiguration {

  @Bean
  DnsManager dnsService(DnsApi dnsApi) {
    return DnsService.create(dnsApi);
  }

}
