package no.jonasandersen.admin.adapter.out.dns;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import no.jonasandersen.admin.application.port.DnsApi;
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
public class CloudflareApi implements DnsApi {

  public static final String AUTO_CREATED = "auto-created:";
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
    List<DnsRecord> dnsRecords = listExistingDnsRecords();

    Optional<DnsRecord> existing = dnsRecords.stream()
        .filter(createdByApplication())
        .filter(dnsRecord -> dnsRecord.name.equals(subdomain + ".jonasandersen.no"))
        .findFirst();

    if (existing.isPresent()) {
      logger.info("DNS record for {} already exists. Updating it.", subdomain);
      updateExistingDnsRecord(ip, existing.get().id(), subdomain, owner);
    } else {
      logger.info("DNS record for {} does not exist. Creating it.", subdomain);
      createNewDnsRecord(ip, owner, subdomain);
    }
  }

  private void createNewDnsRecord(Ip ip, String owner, Subdomain subdomain) {
    restTemplate.postForObject("/zones/%s/dns_records".formatted(properties.cloudflare().zoneId()),
        new Request(ip.value(), subdomain.value(), false, "A", AUTO_CREATED + owner, List.of(), 60),
        Response.class);
  }

  private List<DnsRecord> listExistingDnsRecords() {
    logger.info("Listing existing DNS records");
    Response result = restTemplate.getForObject(
        "/zones/%s/dns_records?type=A".formatted(properties.cloudflare().zoneId()),
        Response.class);

    if (result == null) {
      logger.warn("Failed to list existing DNS records. Returning empty list.");
      return List.of();
    }

    result.result.forEach(dnsRecord -> logger.info("Found DNS record: {}", dnsRecord));

    return result.result();
  }

  private void updateExistingDnsRecord(Ip ip, String dnsRecordId, Subdomain subdomain, String owner) {
    restTemplate.put("/zones/%s/dns_records/%s".formatted(
        properties.cloudflare().zoneId(),
        dnsRecordId), new Request(ip.value(), subdomain.value(),
        false, "A", AUTO_CREATED + owner, List.of(), 60));
  }

  static @NotNull Predicate<DnsRecord> createdByApplication() {
    return result -> (result.comment()) != null && result.comment().startsWith(AUTO_CREATED);
  }

  record Request(String content, String name, boolean proxied, String type, String comment,
                 List<String> tags, int ttl) {

  }

  record DnsRecord(String id, String name, String content, List<String> tags, String comment) {

  }

  record Response(List<DnsRecord> result) {

  }

}
