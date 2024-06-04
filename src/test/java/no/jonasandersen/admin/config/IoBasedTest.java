package no.jonasandersen.admin.config;

import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.application.port.DnsApi;
import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.convention.TestBean;

@SpringBootTest
@AutoConfigureMockMvc
@Import(IoBasedConfiguration.class)
@ActiveProfiles("integration")
@Tag("integration")
@WithMockUser
public class IoBasedTest {

  private static final Logger log = LoggerFactory.getLogger(IoBasedTest.class);

  @TestBean
  protected DnsApi dnsApi;

  private static DnsApi dnsApiTestOverride() {
    log.info("Overriding DnsApi bean with StubDnsApi");
    return new StubDnsApi();
  }

}
