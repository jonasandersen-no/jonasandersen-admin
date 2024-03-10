package no.jonasandersen.admin.adapter.out.linode;

import java.util.Date;
import java.util.List;

public class TestLinodeExchange implements LinodeExchange {

  @Override
  public List<Instance> listInstances() {
    return List.of(new Instance(1L, "Server name", "127.0.0.1", "Running", new Date()));
  }
}
