package no.jonasandersen.admin.adapter.out.linode;

import java.util.Optional;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface LinodeExchange {

  @GetExchange("/linode/instances")
  Page<LinodeInstanceApi> list();

  @GetExchange("/linode/instances/{linodeId}")
  Optional<LinodeInstanceApi> findInstanceById(@PathVariable Long linodeId);

  @GetExchange("/linode/instances/{linodeId}/volumes")
  Page<LinodeVolumeDto> volumes(@PathVariable String linodeId);

  @PostExchange("/volumes/{volumeId}/attach")
  LinodeVolumeDto attach(@PathVariable Long volumeId, @RequestBody AttachVolumeRequestBody body);

  @GetExchange("/volume")
  Page<LinodeVolumeDto> volumes();

  @PostExchange("/linode/instances")
  LinodeInstanceApi createInstance(@RequestBody CreateInstanceRequest request);
}
