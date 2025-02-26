package no.jonasandersen.admin.application.port;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.DefaultSaveFiles;
import no.jonasandersen.admin.domain.SaveFile;
import org.junit.jupiter.api.Test;

class SaveFilesTest {

  @Test
  void createSaveFile() {
    SaveFiles saveFiles = new DefaultSaveFiles();

    SaveFile saveFile = saveFiles.create("A save file name");

    assertThat(saveFile)
        .isNotNull()
        .extracting(SaveFile::getName)
        .isEqualTo("A save file name");
  }

  @Test
  void listAllSaveFiles() {
    SaveFiles saveFiles = new DefaultSaveFiles();

    saveFiles.create("A save file name");

    List<SaveFile> files = saveFiles.findAll();

    assertThat(files)
        .hasSize(1);

  }
}