package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.panfutov.result.Result;
import no.jonasandersen.admin.domain.Ip;
import org.junit.jupiter.api.Test;

class DnsServiceTest {

  @Test
  void failureMessageIfNullIpIsPassed() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(null, "owner", "subdomain");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("ip cannot be null");
  }

  @Test
  void failureMessageIfNullOrBlankOwnerIsPassed() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), null, "subdomain");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");

    result = service.createOrReplaceRecord(Ip.localhostIp(), "", "subdomain");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("owner cannot be null or blank");
  }

  @Test
  void failureMessageIfNullOrBlankSubdomainIsPassed() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo("subdomain cannot be null or blank");
  }

  @Test
  void failureMessageIfSubdomainContainsInvalidCharacters() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", "subdomain!");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo(
        "subdomain contains invalid character: !");

    result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", "subdomain@");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo(
        "subdomain contains invalid character: @");

    result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", "subdomain.");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.firstError().getMessage()).isEqualTo(
        "subdomain contains invalid character: .");
  }

  @Test
  void failureCanContainMultipleErrors() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(null, null, null);
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(3);

    result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", "s.b.d-m!@");
    assertThat(result.isFailure()).isTrue();
    assertThat(result.errorCount()).isEqualTo(4);
  }

  @Test
  void successIfAllParametersAreValid() {
    DnsService service = DnsService.createNull();

    Result<?> result = service.createOrReplaceRecord(Ip.localhostIp(), "owner", "subdomain");
    assertThat(result.isSuccess()).isTrue();
  }
}