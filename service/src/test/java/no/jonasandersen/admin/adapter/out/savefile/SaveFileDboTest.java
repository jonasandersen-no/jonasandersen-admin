package no.jonasandersen.admin.adapter.out.savefile;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SaveFileDboTest {

  @Test
  void validateWithNoOwnerFails() {
    SaveFileDbo dbo = new SaveFileDbo();
    dbo.setName("name");

    assertThatThrownBy(dbo::validate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("owner is null");

  }

  @Test
  void validateWithNoNameFails() {
    SaveFileDbo dbo = new SaveFileDbo();
    dbo.setOwner(1L);

    assertThatThrownBy(dbo::validate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("name is null");
  }
}