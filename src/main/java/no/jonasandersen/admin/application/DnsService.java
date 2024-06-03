package no.jonasandersen.admin.application;

import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.domain.Ip;

public class DnsService {

  private final DnsApi dnsApi;

  public static DnsService create(DnsApi dnsApi) {
    return new DnsService(dnsApi);
  }

  public static DnsService createNull() {
    return new DnsService(new StubDnsApi());
  }

  private DnsService(DnsApi dnsApi) {
    this.dnsApi = dnsApi;
  }

  /**
   * Overwrite or create a DNS record for the given IP.
   *
   * @param ip        The IP to create a DNS record for
   * @param owner     The owner of the DNS record
   * @param subdomain The subdomain to create the DNS record for
   * @throws InvalidParameterException if any of the parameters are invalid
   */
  public void overwriteDnsRecord(Ip ip, String owner, String subdomain) {
    if (ip == null) {
      throw new InvalidParameterException("ip cannot be null");
    }

    if (owner == null || owner.isBlank()) {
      throw new InvalidParameterException("owner cannot be null or blank");
    }

    if (subdomain == null || subdomain.isBlank()) {
      throw new InvalidParameterException("subdomain cannot be null or blank");
    }

    for (char c : subdomain.toCharArray()) {
      if (!Character.isLetterOrDigit(c) && c != '-') {
        throw new InvalidParameterException("subdomain contains invalid characters");
      }
    }

    dnsApi.overwriteDnsRecord(ip, owner, subdomain);
  }
}
