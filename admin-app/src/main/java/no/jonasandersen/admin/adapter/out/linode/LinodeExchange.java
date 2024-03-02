package no.jonasandersen.admin.adapter.out.linode;

import org.springframework.web.service.annotation.GetExchange;

public interface LinodeExchange {

  @GetExchange("/instance/list")
  String listInstances();
}
