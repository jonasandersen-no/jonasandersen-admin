package no.jonasandersen.admin.application.port;

import java.util.List;
import no.jonasandersen.admin.domain.DnsRecord;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;

public interface DnsApi {

  void overwriteDnsRecord(Ip ip, String owner, Subdomain subdomain);

  List<DnsRecord> listExistingDnsRecords();
}
