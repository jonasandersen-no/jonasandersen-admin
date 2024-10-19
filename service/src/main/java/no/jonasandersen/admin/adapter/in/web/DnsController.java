package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.application.DnsService;
import no.jonasandersen.admin.domain.DnsRecord;
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

    List<DnsRecord> dnsRecords = service.listExistingDnsRecords(
        dnsRecord -> dnsRecord.content().endsWith("cfargotunnel.com"));
    model.addAttribute("dnsRecords", dnsRecords);
    return "dns/index";
  }
}
