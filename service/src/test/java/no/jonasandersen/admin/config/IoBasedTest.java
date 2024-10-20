package no.jonasandersen.admin.config;

import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.infrastructure.TestSecurityConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.convention.TestBean;

@SpringBootTest
@AutoConfigureMockMvc
@Import({IoBasedConfiguration.class, TestSecurityConfiguration.class})
public abstract class IoBasedTest extends IntegrationTest {

  @TestBean
  private DnsApi dnsApi;

  // test case body...

  private static DnsApi dnsApi() {
    return new StubDnsApi();
  }
}
