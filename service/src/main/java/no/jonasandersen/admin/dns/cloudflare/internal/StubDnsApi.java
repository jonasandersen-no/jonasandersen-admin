package no.jonasandersen.admin.dns.cloudflare.internal;

import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.dns.api.DnsRecord;
import no.jonasandersen.admin.dns.api.Domain;
import no.jonasandersen.admin.dns.cloudflare.api.DnsApi;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubDnsApi implements DnsApi {

  private static final Logger log = LoggerFactory.getLogger(StubDnsApi.class);
  private List<DnsRecord> dnsRecords = new ArrayList<>();

  public StubDnsApi() {
    log.info("Initializing StubDnsApi");
  }

  public StubDnsApi(List<DnsRecord> dnsRecords) {
    this.dnsRecords = new ArrayList<>(dnsRecords);
  }

  @Override
  public void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain) {
    log.info("Overwriting DNS record for {} with IP {} and owner {}", subdomain, ip, owner);
    dnsRecords.removeIf(dnsRecord -> dnsRecord.name().equals(subdomain.value()));
    dnsRecords.add(new DnsRecord(subdomain.value(), ip.value(), owner));
  }

  @Override
  public List<DnsRecord> listExistingDnsRecords(Domain domain) {
    return List.copyOf(dnsRecords);
  }
}
