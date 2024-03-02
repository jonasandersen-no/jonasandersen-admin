package no.jonasandersen.admin.adapter.out.linode;

public class TestLinodeExchange implements
    LinodeExchange {

  @Override
  public String listInstances() {
    return "Instances";
  }
}
