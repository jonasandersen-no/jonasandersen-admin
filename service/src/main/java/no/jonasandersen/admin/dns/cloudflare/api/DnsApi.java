package no.jonasandersen.admin.dns.cloudflare.api;

import java.util.List;
import no.jonasandersen.admin.dns.api.DnsRecord;
import no.jonasandersen.admin.dns.api.Domain;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;

public interface DnsApi {

  void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain);

  List<DnsRecord> listExistingDnsRecords(Domain domain);
}
