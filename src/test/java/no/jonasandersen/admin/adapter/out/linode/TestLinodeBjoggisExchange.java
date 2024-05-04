package no.jonasandersen.admin.adapter.out.linode;

import java.util.Date;
import java.util.List;

public class TestLinodeBjoggisExchange implements LinodeBjoggisExchange {

  @Override
  public List<Instance> listInstances() {
    return List.of(new Instance(1L, "Server name", "127.0.0.1", "Running", new Date()));
  }

  @Override
  public List<LinodeVolumeDto> getVolumes() {
    return List.of();
  }

  @Override
  public void attachVolume(Long volumeId, Long linodeId) {
    System.out.println("Attaching volume " + volumeId + " to linode " + linodeId);
  }

  @Override
  public void detachVolume(Long volumeId) {
    System.out.println("Detaching volume " + volumeId);
  }
}
