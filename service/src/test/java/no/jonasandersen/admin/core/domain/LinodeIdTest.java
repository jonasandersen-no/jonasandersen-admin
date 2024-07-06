package no.jonasandersen.admin.core.domain;


import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.domain.LinodeId;
import org.junit.jupiter.api.Test;

class LinodeIdTest {

  @Test
  void fromReturnsObject() {
    LinodeId linodeId = LinodeId.from(1L);
    assertThat(linodeId).isNotNull()
        .extracting(LinodeId::id)
        .isEqualTo(1L);
  }
}