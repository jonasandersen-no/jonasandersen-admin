package no.jonasandersen.admin.adapter.out.dns;

import java.util.List;
import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.domain.DnsRecord;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;

public class StubDnsApi implements DnsApi {

  @Override
  public void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain) {

  }

  @Override
  public List<DnsRecord> listExistingDnsRecords() {
    return List.of();
  }
}
