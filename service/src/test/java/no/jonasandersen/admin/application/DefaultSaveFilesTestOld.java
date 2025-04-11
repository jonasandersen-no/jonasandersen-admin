package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFileOld;
import no.jonasandersen.admin.domain.User;
import org.junit.jupiter.api.Test;

class DefaultSaveFilesTestOld {

  @Test
  void findAllOwnedBy() {

    SaveFiles saveFiles = new DefaultSaveFiles();

    saveFiles.add(new SaveFileOld("save-file", User.createUser("mail@example.com")));
    saveFiles.add(new SaveFileOld("save-file2", User.createUser("mail2@example.com")));

    List<SaveFileOld> files = saveFiles.findAllBy(User.createUser("mail@example.com"));

    assertThat(files)
        .hasSize(1)
        .isEqualTo(List.of(new SaveFileOld("save-file", User.createUser("mail@example.com"))));
  }
}