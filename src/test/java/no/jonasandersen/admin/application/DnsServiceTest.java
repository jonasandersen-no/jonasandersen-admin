package no.jonasandersen.admin.application;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import no.jonasandersen.admin.domain.Ip;
import org.junit.jupiter.api.Test;

class DnsServiceTest {


  @Test
  void throwsWhenInvalidIpIsPassed() {
    DnsService dnsService = DnsService.createNull();

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(null, null, null))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("ip cannot be null");

  }

  @Test
  void throwsWhenNullOrBlankOwnerIsPassed() {
    DnsService dnsService = DnsService.createNull();

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), null, null))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("owner cannot be null or blank");

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), "", null))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("owner cannot be null or blank");
  }

  @Test
  void throwsWhenNullOrBlankSubdomainIsPassed() {
    DnsService dnsService = DnsService.createNull();

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), "owner", null))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("subdomain cannot be null or blank");
  }

  @Test
  void throwsWhenSubdomainContainsInvalidCharacters() {
    DnsService dnsService = DnsService.createNull();

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), "owner", "subdomain!"))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("subdomain contains invalid characters");

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), "owner", "subdomain@"))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("subdomain contains invalid characters");

    assertThatThrownBy(() -> dnsService.overwriteDnsRecord(Ip.localhostIp(), "owner", "subdomain."))
        .isInstanceOf(InvalidParameterException.class)
        .hasMessage("subdomain contains invalid characters");
  }
}