package no.jonasandersen.admin.adapter.out.dns;

import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.domain.Ip;

public class StubDnsApi implements DnsApi {

  @Override
  public void overwriteDnsRecord(Ip ip, String owner, String subdomain) {

  }
}
