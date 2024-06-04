package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class DnsServiceIoTest extends IoBasedTest {

  @Autowired
  private DnsService service;

  @Test
  void overriddenDnsApiIsInjectedToDnsService() {
    DnsApi api = service.getDnsApi();

    assertThat(api).isSameAs(this.dnsApi);

  }
}