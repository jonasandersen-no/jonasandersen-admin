package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.panfutov.result.Result;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import org.junit.jupiter.api.Test;

class DnsServiceTest {

  @Test
  void failureMessageIfNullIpIsPassed() {
    DnsService service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(null, "owner", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("ip cannot be null");
  }

  @Test
  void failureMessageIfNullOrBlankOwnerIsPassed() {
    DnsService service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), null, Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");

    result = service.createOrReplaceRecord(Ip.localhostIp(), "", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");
  }

  @Test
  void failureMessageIfNullSubdomainIsPassed() {
    DnsService service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("subdomain cannot be null");
  }

  @Test
  void failureCanContainMultipleErrors() {
    DnsService service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), null, null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(2);

    result = service.createOrReplaceRecord(null, "owner", Subdomain.of("subdomain"));
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(1);
  }

  @Test
  void successIfAllParametersAreValid() {
    DnsService service = DnsService.configureForTest();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", Subdomain.of("subdomain"));
    assertThat(result.isSuccess()).isTrue();
  }
}