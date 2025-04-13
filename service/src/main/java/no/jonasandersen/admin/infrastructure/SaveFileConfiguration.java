package no.jonasandersen.admin.infrastructure;

import no.jonasandersen.admin.application.EventStore;
import no.jonasandersen.admin.application.port.EventStoreRepository;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.SaveFileEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaveFileConfiguration {

  @Bean
  EventStore<SaveFileId, SaveFileEvent, SaveFile> saveFileEventStore(
      EventStoreRepository repository) {
    return EventStore.forSaveFiles(repository);
  }
}
