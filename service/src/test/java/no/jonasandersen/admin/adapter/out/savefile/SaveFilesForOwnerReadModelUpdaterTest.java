package no.jonasandersen.admin.adapter.out.savefile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.SaveFileCreatedEvent;
import no.jonasandersen.admin.domain.SaveFileId;
import no.jonasandersen.admin.domain.SaveFilesForOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

class SaveFilesForOwnerReadModelUpdaterTest extends IoBasedTest {

  @Autowired private MongoTemplate mongoTemplate;

  @Test
  void saveFileCreatedEventInsertsReadModel() {

    var updater = new SaveFilesForOwnerReadModelUpdater(mongoTemplate);

    updater.handleSaveFileCreated(
        new SaveFileCreatedEvent(
            new SaveFileId(UUID.randomUUID()),
            "SaveFilesForOwnerReadModelUpdater",
            "SaveFilesForOwnerReadModelUpdater"));

    SaveFilesForOwner entity =
        mongoTemplate.findOne(
            new Query(Criteria.where("owner").is("SaveFilesForOwnerReadModelUpdater")),
            SaveFilesForOwner.class,
            "saveFilesByOwner");

    assertThat(entity.owner()).isEqualTo("SaveFilesForOwnerReadModelUpdater");
    assertThat(entity.saveFilesNames()).isEqualTo(List.of("SaveFilesForOwnerReadModelUpdater"));
  }
}
