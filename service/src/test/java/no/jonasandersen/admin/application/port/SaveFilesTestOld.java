package no.jonasandersen.admin.application.port;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.DefaultSaveFiles;
import no.jonasandersen.admin.domain.SaveFileOld;
import org.junit.jupiter.api.Test;

class SaveFilesTestOld {

  @Test
  void createSaveFile() {
    SaveFiles saveFiles = new DefaultSaveFiles();

    SaveFileOld saveFileOld = saveFiles.create("A save file name");

    assertThat(saveFileOld)
        .isNotNull()
        .extracting(SaveFileOld::getName)
        .isEqualTo("A save file name");
  }

  @Test
  void listAllSaveFiles() {
    SaveFiles saveFiles = new DefaultSaveFiles();

    saveFiles.create("A save file name");

    List<SaveFileOld> files = saveFiles.findAll();

    assertThat(files)
        .hasSize(1);

  }
}