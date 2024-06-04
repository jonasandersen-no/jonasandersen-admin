package no.jonasandersen.admin;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.config.IoBasedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class AdminApplicationTests extends IoBasedTest {

  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoads() {
    assertThat(context.getBean("dnsApi"))
        .isSameAs(this.dnsApi)
        .isInstanceOf(StubDnsApi.class);
  }

}
