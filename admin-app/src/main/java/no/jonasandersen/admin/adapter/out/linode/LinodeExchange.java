package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

interface LinodeExchange {

  @GetExchange("/linode/instances")
  Page<LinodeInstanceApi> list();

  @GetExchange("/linode/instances/{linodeId}/volumes")
  Page<LinodeVolumeDto> volumes(@PathVariable String linodeId);
}
