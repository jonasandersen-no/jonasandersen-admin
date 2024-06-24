package no.jonasandersen.admin.adapter.out.linode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import no.jonasandersen.admin.OutputListener;
import no.jonasandersen.admin.OutputTracker;
import no.jonasandersen.admin.adapter.out.linode.api.model.LinodeInstanceApi;
import no.jonasandersen.admin.adapter.out.linode.api.model.Page;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Alerts;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Backups;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Schedule;
import no.jonasandersen.admin.adapter.out.linode.api.model.instance.Specs;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.InstanceDetails;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeVolume;
import no.jonasandersen.admin.domain.VolumeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class LinodeServerApi implements ServerApi {

  private final Logger logger = LoggerFactory.getLogger(LinodeServerApi.class);
  private final OutputListener<LinodeInstanceApi> outputListener = new OutputListener<>();
  private final LinodeExchange linodeExchange;

  public static LinodeServerApi create(LinodeExchange linodeExchange) {
    return new LinodeServerApi(linodeExchange);
  }

  public static LinodeServerApi configureForTest(Function<Config, Config> configure) {
    Config config = configure.apply(new Config());
    return configureForTest(config);
  }

  public static LinodeServerApi configureForTest(Config config) {
    return createNull(config.linodeInstances);
  }

  public static LinodeServerApi createNull() {
    return new LinodeServerApi(new StubLinodeExchange());
  }

  public static LinodeServerApi createNull(List<LinodeInstanceApi> instances) {
    return createNull(instances, List.of());
  }

  public static LinodeServerApi createNull(List<LinodeInstanceApi> instances,
      List<LinodeVolumeDto> volumes) {
    return new LinodeServerApi(new StubLinodeExchange(instances, volumes));
  }

  private LinodeServerApi(LinodeExchange linodeExchange) {
    this.linodeExchange = linodeExchange;
  }

  @Override
  public LinodeInstance createInstance(InstanceDetails instanceDetails) {
    logger.info("Creating instance with details: {}", instanceDetails);

    LinodeInstanceApi linodeInstanceApi = linodeExchange.createInstance(
        CreateInstanceRequest.from(instanceDetails));
    outputListener.track(linodeInstanceApi);
    return linodeInstanceApi.toDomain();
  }

  @Override
  public List<LinodeVolume> getVolumes() {
    Page<LinodeVolumeDto> volumes = linodeExchange.volumes();

    return volumes.data().stream().map(
            volume -> {
              VolumeId volumeId = new VolumeId(volume.id());
              LinodeId linodeId =
                  volume.linodeId() == null ? null : new LinodeId(volume.linodeId());

              return new LinodeVolume(volumeId, volume.label(), volume.status(), linodeId);
            })
        .toList();
  }

  @Override
  public List<LinodeInstance> getInstances() {
    Page<LinodeInstanceApi> list = linodeExchange.list();

    List<LinodeInstanceApi> data = list.data();

    return data.stream()
        .map(LinodeInstanceApi::toDomain)
        .toList();
  }

  @Override
  public Optional<LinodeInstance> findInstanceById(LinodeId linodeId) {
    Optional<LinodeInstanceApi> linodeInstanceApi = linodeExchange.findInstanceById(linodeId.id());

    return linodeInstanceApi
        .map(LinodeInstanceApi::toDomain);
  }

  @Override
  public List<LinodeVolume> getVolumesByInstance(LinodeId linodeId) {
    Page<LinodeVolumeDto> volumes = linodeExchange.volumes(String.valueOf(linodeId.id()));

    return volumes.data().stream()
        .map(
            volume -> {
              VolumeId volumeId = new VolumeId(volume.id());
              LinodeId linodeId1 = new LinodeId(volume.linodeId());
              return new LinodeVolume(volumeId, volume.label(), volume.status(), linodeId1);
            })
        .toList();
  }

  @Override
  public void attachVolume(LinodeId linodeId, VolumeId volumeId) {
    AttachVolumeRequestBody body = new AttachVolumeRequestBody();
    body.setLinodeId(linodeId.id());
    body.setPersistAcrossBoots(false);

    linodeExchange.attach(volumeId.id(), body);
  }

  @Override
  public boolean deleteInstance(LinodeId linodeId) {
    ResponseEntity<Void> response = linodeExchange.deleteInstance(linodeId.id());

    return response.getStatusCode().is2xxSuccessful();
  }

  public OutputTracker<LinodeInstanceApi> track() {
    return outputListener.createTracker();
  }

  // NULLABLES

  public static class Config {

    private final List<LinodeInstanceApi> linodeInstances = new ArrayList<>();

    public Config addInstance(LinodeInstanceApi... linodeInstance) {
      this.linodeInstances.addAll(List.of(linodeInstance));
      return this;
    }
  }

  private static class StubLinodeExchange implements LinodeExchange {

    private final static Logger logger = LoggerFactory.getLogger(StubLinodeExchange.class);

    private final List<LinodeInstanceApi> instances;
    private final List<LinodeVolumeDto> volumes;
    private Long id = 1L;

    public StubLinodeExchange() {
      instances = new ArrayList<>();
      volumes = new ArrayList<>();
    }

    public StubLinodeExchange(List<LinodeInstanceApi> instances, List<LinodeVolumeDto> volumes) {
      this.instances = new ArrayList<>(instances);
      this.volumes = new ArrayList<>(volumes);
    }

    @Override
    public Page<LinodeInstanceApi> list() {
      return new Page<>(List.copyOf(instances), 0, 1, instances.size());
    }

    @Override
    public Optional<LinodeInstanceApi> findInstanceById(Long linodeId) {
      return instances.stream()
          .filter(instance -> linodeId.equals(instance.id()))
          .findFirst();
    }

    @Override
    public Page<LinodeVolumeDto> volumes(String linodeId) {
      List<LinodeVolumeDto> filteredList = volumes.stream()
          .filter(volume -> volume.linodeId().equals(Long.valueOf(linodeId)))
          .toList();

      return new Page<>(List.copyOf(filteredList), 0, 1, filteredList.size());
    }

    @Override
    public LinodeVolumeDto attach(Long volumeId, AttachVolumeRequestBody body) {
      logger.info("STUB: Attaching volume {} to linode {}", volumeId, body.getLinodeId());
      return null;
    }

    @Override
    public Page<LinodeVolumeDto> volumes() {
      return new Page<>(List.copyOf(volumes), 0, 1, volumes.size());
    }

    @Override
    public LinodeInstanceApi createInstance(CreateInstanceRequest request) {
      LinodeInstanceApi instance = new LinodeInstanceApi(id++,
          request.label(),
          "group",
          "running",
          LocalDateTime.now(),
          LocalDateTime.now(),
          request.type(),
          List.of("127.0.0.1"),
          "::1",
          request.image(),
          request.region(),
          new Specs(81920, 4096, 2, 0, 4000),
          new Alerts(100, 10, 10, 80, 10000),
          new Backups(true, true, new Schedule("Saturday", "W22"), LocalDateTime.now()),
          "kvm",
          false,
          List.copyOf(request.tags()),
          "21e5aaacb4064de1951324ce58a2c41b",
          false);

      if (request.volume()) {
        LinodeVolumeDto volume = new LinodeVolumeDto(id++, "volume", "active", instance.id());
        volumes.add(volume);
      }

      instances.add(instance);
      return instance;
    }

    @Override
    public ResponseEntity<Void> deleteInstance(Long linodeId) {
      boolean deleted = instances.removeIf(instance -> instance.id() == linodeId);
      if (deleted) {
        return ResponseEntity.ok(null);
      }
      return ResponseEntity.badRequest().build();
    }
  }

}
