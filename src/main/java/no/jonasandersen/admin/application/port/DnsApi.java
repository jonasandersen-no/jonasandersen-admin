package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.Ip;

public interface DnsApi {

  void overwriteDnsRecord(Ip ip, String owner, String subdomain);
}
