package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.dns.api.DnsManager;
import no.jonasandersen.admin.dns.api.DnsRecords;
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

  record Domain(String rootDomain, DnsRecords dnsRecords) {

  }

  @GetMapping
  public String getDnsRecords(Model model) {

    DnsRecords dnsRecords = manager.listExistingDnsRecords(
        dnsRecord -> dnsRecord.content().endsWith("cfargotunnel.com"));
    model.addAttribute("dnsRecords", dnsRecords.sortByContent().records());
    return "dns/index";
  }
}
