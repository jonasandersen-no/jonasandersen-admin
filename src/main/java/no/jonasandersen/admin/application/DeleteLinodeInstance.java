package no.jonasandersen.admin.application;

import java.util.Optional;
import no.jonasandersen.admin.application.port.ServerApi;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import org.springframework.stereotype.Service;

@Service
public class DeleteLinodeInstance {

  private final ServerApi api;

  public DeleteLinodeInstance(ServerApi api) {
    this.api = api;
  }

  public boolean delete(LinodeId linodeId) {
    Optional<LinodeInstance> instance = api.findInstanceById(linodeId);

    if (instance.isPresent()) {
      api.deleteInstance(linodeId);
      return true;
    }
    return false;
  }
}
