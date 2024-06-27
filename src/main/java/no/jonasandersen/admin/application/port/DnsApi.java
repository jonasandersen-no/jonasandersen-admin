package no.jonasandersen.admin.application.port;

import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;

public interface DnsApi {

  void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain);
}
