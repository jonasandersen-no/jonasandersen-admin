package no.jonasandersen.admin.adapter.in.listeners;

import no.jonasandersen.admin.application.ServerGenerator;
import no.jonasandersen.admin.domain.InstanceCreatedEvent;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class ServerGeneratorListener {

  private static final Logger log = LoggerFactory.getLogger(ServerGeneratorListener.class);
  private final ServerGenerator serverGenerator;
  private final AdminProperties properties;

  public ServerGeneratorListener(ServerGenerator serverGenerator, AdminProperties properties) {
    this.serverGenerator = serverGenerator;
    this.properties = properties;
  }

  @Async
  @TransactionalEventListener
  public void onInstanceCreated(InstanceCreatedEvent event) {
    log.info("Received instance created event: {}", event);
    String password = properties.minecraft().password();
    serverGenerator.install(event.linodeId(), SensitiveString.of(password), event.serverType());
  }
}
