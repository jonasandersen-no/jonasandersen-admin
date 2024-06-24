package no.jonasandersen.admin.adapter.in.listeners;

import no.jonasandersen.admin.application.LinodeVolumeService;
import no.jonasandersen.admin.domain.InstanceCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class LinodeVolumeServiceListener {

  private static final Logger log = LoggerFactory.getLogger(LinodeVolumeServiceListener.class);
  private final LinodeVolumeService linodeVolumeService;

  public LinodeVolumeServiceListener(LinodeVolumeService linodeVolumeService) {
    this.linodeVolumeService = linodeVolumeService;
  }

  @ApplicationModuleListener
  public void onInstanceCreated(InstanceCreatedEvent event) {
    log.info("Received instance created event: {}", event);
    linodeVolumeService.attachVolume(event.linodeId(), event.volumeId());
  }

}
