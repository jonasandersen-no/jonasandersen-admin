package no.jonasandersen.admin.dns.internal;

import com.panfutov.result.Error;
import com.panfutov.result.GenericError;
import com.panfutov.result.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import no.jonasandersen.admin.dns.api.DnsManager;
import no.jonasandersen.admin.dns.api.DnsRecord;
import no.jonasandersen.admin.dns.api.DnsRecords;
import no.jonasandersen.admin.dns.cloudflare.api.DnsApi;
import no.jonasandersen.admin.dns.cloudflare.internal.StubDnsApi;
import no.jonasandersen.admin.domain.Ip;
import no.jonasandersen.admin.domain.Subdomain;
import org.jetbrains.annotations.Nullable;

class DnsService implements DnsManager {

  private final DnsApi dnsApi;

  @Override
  public Result<Void> createOrReplaceRecord(Ip ip, String owner, Subdomain subdomain) {
    Result<Void> errors = validate(ip, owner, subdomain);

    if (errors != null) {
      return errors;
    }

    dnsApi.overwriteDnsRecord(ip, owner, subdomain);
    return Result.successVoid();
  }

  @Override
  public DnsRecords listExistingDnsRecords() {
    return listExistingDnsRecords(dnsRecord -> true);
  }

  @Override
  public DnsRecords listExistingDnsRecords(Predicate<DnsRecord> filter) {
    List<DnsRecord> dnsRecords = dnsApi.listExistingDnsRecords();
    return new DnsRecords(dnsRecords.stream()
        .filter(filter)
        .toList());
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

  public static DnsManager create(DnsApi dnsApi) {
    return new DnsService(dnsApi);
  }

  public static DnsManager configureForTest() {
    return configureForTest(Function.identity());
  }

  public static DnsManager configureForTest(Function<Config, Config> config) {
    Config cfg = new Config();
    config.apply(cfg);
    return new DnsService(new StubDnsApi(cfg.dnsRecords));
  }

  private DnsService(DnsApi dnsApi) {
    this.dnsApi = dnsApi;
  }

  public static class Config {

    private final List<DnsRecord> dnsRecords = new ArrayList<>();

    public Config addDnsRecord(DnsRecord dnsRecord) {
      dnsRecords.add(dnsRecord);
      return this;
    }
  }
}
