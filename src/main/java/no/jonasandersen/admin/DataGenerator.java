package no.jonasandersen.admin;

import java.util.List;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeInstanceRepository;
import no.jonasandersen.admin.adapter.out.linode.JdbcLinodeVolumeRepository;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeInstanceDbo;
import no.jonasandersen.admin.adapter.out.linode.model.api.db.LinodeVolumeDbo;
import org.instancio.Instancio;
import org.instancio.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataGenerator implements ApplicationListener<ApplicationStartedEvent> {

  private static final Boolean OVERWRITE_DATA = false;
  private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);
  private final JdbcLinodeInstanceRepository instanceRepository;
  private final JdbcLinodeVolumeRepository volumeRepository;

  public DataGenerator(JdbcLinodeInstanceRepository instanceRepository,
      JdbcLinodeVolumeRepository volumeRepository) {
    this.instanceRepository = instanceRepository;
    this.volumeRepository = volumeRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {

    if (OVERWRITE_DATA) {
      log.info("Deleting all instances and volumes...");
      volumeRepository.deleteAll();
      instanceRepository.deleteAll();
    }

    long l = instanceRepository.countAll();

    if (l == 0) {
      log.info("Generating instances...");
      List<LinodeInstanceDbo> instances = instanceRepository.saveAll(
          Instancio.ofList(LinodeInstanceDbo.class)
              .size(10)
              .ignore(Select.field(LinodeInstanceDbo::id))
              .generate(Select.field(LinodeInstanceDbo::status),
                  generators -> generators.oneOf("booting", "running", "stopped", "provisioning"))
              .generate(Select.field(LinodeInstanceDbo::tags),
                  generators -> generators.oneOf("webserver", "database", "cache", "jump_host",
                      "bastion", "jenkins", "monitoring", "logging", "qa", "prod", "windows",
                      "linux", "freebsd", "containerized", "bare_metal", "high_availability",
                      "disaster_recovery", "research", "training", "inactive"))
              .generate(Select.field(LinodeInstanceDbo::label),
                  generators -> generators.text().pattern("ubuntu-#a#a#a#a#a#a"))
              .generate(Select.field(LinodeInstanceDbo::ips), generators -> generators.net().ip4())
              .create()
      );

      instances.forEach(instance -> {
        volumeRepository.save(new LinodeVolumeDbo(null, instance.volumeNames(),"active", instance.id()));
      });
    }
  }
}
