package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFile;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.Test;

class DefaultSaveFilesTest {

  @Test
  void findAllOwnedBy() {

    SaveFiles saveFiles = new DefaultSaveFiles();

    saveFiles.add(new SaveFile("save-file", User.createUser("mail@example.com")));
    saveFiles.add(new SaveFile("save-file2", User.createUser("mail2@example.com")));

    List<SaveFile> files = saveFiles.findAllBy(User.createUser("mail@example.com"));

    assertThat(files).hasSize(1)
        .isEqualTo(List.of(new SaveFile("save-file", User.createUser("mail@example.com"))));
  }
}