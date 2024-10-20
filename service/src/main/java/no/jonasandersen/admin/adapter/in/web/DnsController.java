package no.jonasandersen.admin.adapter.in.web;

import no.jonasandersen.admin.application.DnsService;
import no.jonasandersen.admin.domain.DnsRecords;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dns")
public class DnsController {

  private final DnsService service;

  public DnsController(DnsService service) {
    this.service = service;
  }

  @GetMapping
  public String getDnsRecords(Model model) {

    DnsRecords dnsRecords = service.listExistingDnsRecords(
        dnsRecord -> dnsRecord.content().endsWith("cfargotunnel.com"));
    model.addAttribute("dnsRecords", dnsRecords.sortByContent().records());
    return "dns/index";
  }
}
