package no.jonasandersen.admin.application;

import com.panfutov.result.Error;
import com.panfutov.result.GenericError;
import com.panfutov.result.Result;
import java.util.ArrayList;
import java.util.List;
import no.jonasandersen.admin.adapter.out.dns.StubDnsApi;
import no.jonasandersen.admin.application.port.DnsApi;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import org.jetbrains.annotations.Nullable;

public class DnsService {

  private final DnsApi dnsApi;

  public static DnsService create(DnsApi dnsApi) {
    return new DnsService(dnsApi);
  }

  public static DnsService configureForTest() {
    return new DnsService(new StubDnsApi());
  }

  private DnsService(DnsApi dnsApi) {
    this.dnsApi = dnsApi;
  }

  /**
   * Overwrite or create a DNS record for the given IP.
   *
   * @param ip The IP to create a DNS record for
   * @param owner The owner of the DNS record
   * @param subdomain The subdomain to create the DNS record for
   * @return A Result containing either a list of errors or void
   */
  public Result<Void> createOrReplaceRecord(Ip ip, String owner, Subdomain subdomain) {
    Result<Void> errors = validate(ip, owner, subdomain);

    if (errors != null) {
      return errors;
    }

    dnsApi.overwriteDnsRecord(ip, owner, subdomain);
    return Result.successVoid();
  }

  private static @Nullable Result<Void> validate(Ip ip, String owner, Subdomain subdomain) {
    List<GenericError> errors = new ArrayList<>();

    if (ip == null) {
      errors.add(new Error("ip cannot be null"));
    }

    if (owner == null || owner.isBlank()) {
      errors.add(new Error("owner cannot be null or blank"));
    }

    if (subdomain == null) {
      errors.add(new Error("subdomain cannot be null"));
    }

    if (!errors.isEmpty()) {
      return Result.failure(errors);
    }
    return null;
  }
}
