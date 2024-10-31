package no.jonasandersen.admin.dns.api;

import com.panfutov.result.Result;
import java.util.function.Predicate;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;

public interface DnsManager {

  /**
   * Overwrite or create a DNS record for the given IP.
   *
   * @param ip        The IP to create a DNS record for
   * @param owner     The owner of the DNS record
   * @param subdomain The subdomain to create the DNS record for
   * @return A Result containing either a list of errors or void
   */
  Result<Void> createOrReplaceRecord(Ip ip, String owner, Subdomain subdomain);

  DnsRecords listExistingDnsRecords();

  DnsRecords listExistingDnsRecords(Predicate<DnsRecord> filter);
}
