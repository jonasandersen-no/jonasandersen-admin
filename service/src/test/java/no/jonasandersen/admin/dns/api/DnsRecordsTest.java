package no.jonasandersen.admin.dns.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DnsRecordsTest {

  @Test
  void sortByContent() {
    List<DnsRecord> unsortedRecords = new ArrayList<>();
    unsortedRecords.add(new DnsRecord("name", "def", "type"));
    unsortedRecords.add(new DnsRecord("name", "abc", "type"));
    DnsRecords dnsRecords = new DnsRecords(unsortedRecords);

    DnsRecords sortedRecords = dnsRecords.sortByContent();

    assertThat(sortedRecords.records()).containsExactly(
        new DnsRecord("name", "abc", "type"),
        new DnsRecord("name", "def", "type"));
  }

  @Test
  void dnsRecordsListIsImmutable() {
    List<DnsRecord> unsortedRecords = new ArrayList<>();
    unsortedRecords.add(new DnsRecord("name", "def", "type"));
    DnsRecords dnsRecords = new DnsRecords(unsortedRecords);

    assertThrows(UnsupportedOperationException.class, () -> dnsRecords.records().add(new DnsRecord("name", "def", "type")));
  }
}