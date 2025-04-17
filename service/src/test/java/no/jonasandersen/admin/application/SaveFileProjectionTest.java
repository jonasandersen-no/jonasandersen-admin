package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.adapter.out.savefile.SaveFileProjection;
import no.jonasandersen.admin.config.IoBasedTest;
import no.jonasandersen.admin.domain.SaveFilesForOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

class SaveFileProjectionTest extends IoBasedTest {

  @Autowired private MongoTemplate mongoTemplate;

  @Test
  void projectSaveFileViewWithSingleSaveFileForOwner() {
    mongoTemplate.insert(
        new SaveFilesForOwner("jonas", List.of("saveFileName1")), "saveFilesByOwner");

    SaveFileProjection projection = new SaveFileProjection(mongoTemplate);

    SaveFilesForOwner view = projection.saveFilesForOwner("jonas");

    assertThat(view.owner()).isEqualTo("jonas");
    assertThat(view.saveFilesNames()).containsExactly("saveFileName1");
  }
}
