package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.dns.api.DnsManager;
import no.jonasandersen.admin.dns.api.DnsRecords;
import no.jonasandersen.admin.dns.api.Domain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dns")
public class DnsController {

  private final DnsManager manager;

  public DnsController(DnsManager manager) {
    this.manager = manager;
  }

  record DomainRecord(String rootDomain, DnsRecords dnsRecords) {

  }

  @GetMapping
  public String getDnsRecords(Model model) {

    DnsRecords dnsRecords = manager.listExistingDnsRecords(
        dnsRecord -> dnsRecord.content().endsWith("cfargotunnel.com"),
        Domain.JONASANDERSEN_NO);

    DnsRecords dnsRecords2 = manager.listExistingDnsRecords(
        dnsRecord -> dnsRecord.content().endsWith("cfargotunnel.com"),
        Domain.BJOGGIS_COM);

    model.addAttribute("domains", List.of(
        new DomainRecord("jonasandersen.no", dnsRecords.sortByContent()),
        new DomainRecord("bjoggis.com", dnsRecords2.sortByContent())));
    return "dns/index";
  }
}
