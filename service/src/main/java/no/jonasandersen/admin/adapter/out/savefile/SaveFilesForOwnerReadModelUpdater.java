package no.jonasandersen.admin.adapter.out.savefile;

import no.jonasandersen.admin.domain.SaveFileCreatedEvent;
import no.jonasandersen.admin.domain.SaveFilesForOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
class SaveFilesForOwnerReadModelUpdater {

  private static final Logger log =
      LoggerFactory.getLogger(SaveFilesForOwnerReadModelUpdater.class);
  private final MongoTemplate mongoTemplate;

  SaveFilesForOwnerReadModelUpdater(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @EventListener
  void handleSaveFileCreated(SaveFileCreatedEvent event) {
    log.info("Read model updater received SaveFileCreatedEvent for: {}", event.id().id());

    Query query = new Query(Criteria.where("owner").is(event.owner()));
    Update update = new Update().addToSet("saveFilesNames", event.name());

    // Upsert: If a document for the owner exists, add the saveFileId to the set.
    // If it doesn't exist, create a new document with the owner and the saveFileId in the list.
    mongoTemplate.upsert(query, update, SaveFilesForOwner.class, "saveFilesByOwner");
  }

  //    @EventListener
  //    public void handleSaveFileRenamed(SaveFileRenamedEvent event) {
  //        System.out.println("Read model updater received SaveFileRenamedEvent for: " +
  // event.getSaveFileId());
  //
  //        Query query = new Query(Criteria.where("saveFileId").is(event.getSaveFileId()));
  //        Update update = new Update().set("name", event.getNewName());
  //        mongoTemplate.updateFirst(query, update, "saveFiles");
  //    }

  // You would add similar event listener methods for other relevant events
  // like SaveFileDeletedEvent, etc.
}
