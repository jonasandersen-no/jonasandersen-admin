package no.jonasandersen.admin.adapter.out.linode;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;

public interface LinodeBjoggisExchange {

  @GetExchange("/instance/list")
  List<Instance> listInstances();

  @GetExchange("/volume")
  List<LinodeVolumeDto> getVolumes();

  @PatchExchange("/volume")
  void attachVolume(@RequestParam Long volumeId, @RequestParam Long linodeId);

  @PatchExchange("/volume/detach")
  void detachVolume(@RequestParam Long volumeId);
}
