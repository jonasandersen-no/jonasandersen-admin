package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import org.springframework.web.service.annotation.GetExchange;

public interface LinodeExchange {

  @GetExchange("/instance/list")
  List<Instance> listInstances();
}
