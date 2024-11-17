package no.jonasandersen.admin.dns.internal;

import static org.assertj.core.api.Assertions.assertThat;

import com.panfutov.result.Result;
import java.util.UUID;
import no.jonasandersen.admin.dns.api.DnsManager;
import no.jonasandersen.admin.dns.api.DnsRecord;
import no.jonasandersen.admin.dns.api.DnsRecords;
import no.jonasandersen.admin.dns.api.Domain;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import org.junit.jupiter.api.Test;

class DnsServiceTest {

  @Test
  void failureMessageIfNullIpIsPassed() {
    DnsManager service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(null, "owner", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("ip cannot be null");
  }

  @Test
  void failureMessageIfNullOrBlankOwnerIsPassed() {
    DnsManager service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), null, Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");

    result = service.createOrReplaceRecord(Ip.localhostIp(), "", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");
  }

  @Test
  void failureMessageIfNullSubdomainIsPassed() {
    DnsManager service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("subdomain cannot be null");
  }

  @Test
  void failureCanContainMultipleErrors() {
    DnsManager service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), null, null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(2);

    result = service.createOrReplaceRecord(null, "owner", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(1);
  }

  @Test
  void successIfAllParametersAreValid() {
    DnsManager service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", Subdomain.of("subdomain"));
    assertThat(result.isSuccess()).isTrue();
  }

  @Test
  void listExistingRecords() {
    DnsManager service = DnsService.configureForTest(
        config -> config.addDnsRecord(new DnsRecord("test", UUID.randomUUID().toString(), "CNAME")));

    DnsRecords dnsRecords = service.listExistingDnsRecords(Domain.JONASANDERSEN_NO);
    assertThat(dnsRecords.records()).hasSize(1);
  }
}