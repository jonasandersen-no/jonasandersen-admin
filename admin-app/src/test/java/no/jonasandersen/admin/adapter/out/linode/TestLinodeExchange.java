package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;

public class TestLinodeExchange implements
    LinodeExchange {

  @Override
  public Page<LinodeInstanceApi> list() {
    return null;
  }
}
