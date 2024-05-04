package no.jonasandersen.admin.core.minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import no.jonasandersen.admin.core.domain.LinodeId;
import no.jonasandersen.admin.core.domain.LinodeInstance;
import no.jonasandersen.admin.core.domain.LinodeVolume;
import no.jonasandersen.admin.core.minecraft.port.ServerApi;

public class InMemoryServerApi implements ServerApi {

  private static final AtomicLong SEQUENCE = new AtomicLong(1);
  private final Map<LinodeId, LinodeInstance> instances = new HashMap<>();
  private final Map<LinodeId, LinodeVolume> volumes = new HashMap<>();

  @Override
  public LinodeInstance createInstance(String label, String tags) {
    LinodeInstance linodeInstance = new LinodeInstance(LinodeId.from(SEQUENCE.getAndIncrement()),
        List.of("127.0.0.1"), "running", label, List.of(tags), List.of());

    instances.put(linodeInstance.linodeId(), linodeInstance);
    return linodeInstance;
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    return List.copyOf(volumes.values());
  }

  @Override
  public List<LinodeInstance> getInstances() {
    return List.copyOf(instances.values());
  }

  @Override
  public LinodeInstance getInstanceById(LinodeId linodeId) {
    return instances.get(linodeId);
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    return volumes.values().stream()
        .filter(volume -> volume.linodeId().equals(linodeId))
        .toList();
  }
}
