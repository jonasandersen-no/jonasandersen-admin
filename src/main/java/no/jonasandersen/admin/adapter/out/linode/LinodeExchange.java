package no.jonasandersen.admin.adapter.out.linode;

import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import no.jonasandersen.admin.domain.InstanceDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface LinodeExchange {

  @GetExchange("/linode/instances")
  Page<LinodeInstanceApi> list();

  @GetExchange("/linode/instances/{linodeId}")
  LinodeInstanceApi getInstanceById(@PathVariable Long linodeId);

  @GetExchange("/linode/instances/{linodeId}/volumes")
  Page<LinodeVolumeDto> volumes(@PathVariable String linodeId);

  @GetExchange("/volume")
  Page<LinodeVolumeDto> volumes();

  @PostExchange("/v4/linode/instances")
  LinodeInstanceApi createInstance(@RequestBody InstanceDetails instanceDetails);
}
