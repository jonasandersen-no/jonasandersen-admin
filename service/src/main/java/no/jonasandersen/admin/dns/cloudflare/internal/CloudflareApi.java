package no.jonasandersen.admin.dns.cloudflare.internal;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import no.jonasandersen.admin.dns.api.DnsRecord;
import no.jonasandersen.admin.dns.cloudflare.api.DnsApi;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class CloudflareApi implements DnsApi {

  private final Logger logger = LoggerFactory.getLogger(CloudflareApi.class);
  private final AdminProperties properties;
  private final RestTemplate restTemplate;

  public CloudflareApi(AdminProperties properties) {
    this.properties = properties;

    restTemplate = new RestTemplateBuilder()
        .defaultHeader("Authorization", "Bearer " + properties.cloudflare().apiKey())
        .rootUri("https://api.cloudflare.com/client/v4")
        .build();
  }

  @Override
  public void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain) {
    List<CloudflareDnsRecord> cloudflareDnsRecords = listExistingDnsRecordsInternal();

    Optional<CloudflareDnsRecord> existing = cloudflareDnsRecords.stream()
        .filter(createdByApplication())
        .filter(cloudflareDnsRecord -> cloudflareDnsRecord.name.equals(subdomain + ".jonasandersen.no"))
        .findFirst();

    if (existing.isPresent()) {
      logger.info("DNS record for {} already exists. Updating it.", subdomain);
      updateExistingDnsRecord(ip, subdomain, owner);
    } else {
      logger.info("DNS record for {} does not exist. Creating it.", subdomain);
      createNewDnsRecord(ip, owner, subdomain);
    }
  }

  private void createNewDnsRecord(Ip ip, String owner, Subdomain subdomain) {
    restTemplate.postForObject("/zones/%s/dns_records".formatted(properties.cloudflare().zoneId()),
        new Request(ip.value(), subdomain.value(), false, "A", "Created by " + owner, List.of("auto-created"), 60),
        Response.class);
  }

  private List<CloudflareDnsRecord> listExistingDnsRecordsInternal() {
    Response result = restTemplate.getForObject("/zones/%s/dns_records".formatted(properties.cloudflare().zoneId()),
        Response.class);

    if (result == null) {
      logger.warn("Failed to list existing DNS records. Returning empty list.");
      return List.of();
    }

    return result.result();
  }


  @Override
  public List<DnsRecord> listExistingDnsRecords() {
    Response result = restTemplate.getForObject("/zones/%s/dns_records".formatted(properties.cloudflare().zoneId()),
        Response.class);

    if (result == null) {
      logger.warn("Failed to list existing DNS records. Returning empty list.");
      return List.of();
    }

    return result.result()
        .stream().map(CloudflareDnsRecord::toDomain)
        .toList();
  }

  private void updateExistingDnsRecord(Ip ip, Subdomain subdomain, String owner) {
    restTemplate.put("/zones/%s/dns_records/%s".formatted(properties.cloudflare().zoneId(),
        properties.cloudflare().dnsRecordId()), new Request(ip.value(), subdomain.value(),
        false, "A", "auto-created:" + owner, List.of(), 60));
  }

  static @NotNull Predicate<CloudflareDnsRecord> createdByApplication() {
    return result -> (result.comment()) != null && (result.comment()
        .equalsIgnoreCase("Updated by Spillhuset") || result.comment().contains("auto-created:"));
  }

  record Request(String content, String name, boolean proxied, String type, String comment,
                 List<String> tags, int ttl) {

  }

  record CloudflareDnsRecord(String id, String name, String content, List<String> tags, String comment, String type) {

    DnsRecord toDomain() {
      return new DnsRecord(name, content, type);
    }
  }

  record Response(List<CloudflareDnsRecord> result) {

  }

}
