package no.jonasandersen.admin.dns.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record DnsRecords(List<DnsRecord> records) {

  public DnsRecords {
    records = List.copyOf(records);
  }

  public DnsRecords sortByContent() {
    ArrayList<DnsRecord> sortedList = new ArrayList<>(records);
    sortedList.sort(Comparator.comparing(DnsRecord::content));
    return new DnsRecords(sortedList);
  }
}
