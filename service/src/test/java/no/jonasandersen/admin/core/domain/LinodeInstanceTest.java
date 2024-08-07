package no.jonasandersen.admin.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import no.jonasandersen.admin.domain.LinodeId;
import no.jonasandersen.admin.domain.LinodeInstance;
import no.jonasandersen.admin.domain.LinodeSpecs;
import org.junit.jupiter.api.Test;

class LinodeInstanceTest {

  @Test
  void ownerReturnsOwnerNameFromTag() {
    LinodeInstance instance = new LinodeInstance(new LinodeId(0L), List.of(), "", "",
        List.of("owner:principalName"), List.of(), new LinodeSpecs(0, 0));

    assertThat(instance.owner()).isEqualTo("principalName");
  }

  @Test
  void ownerReturnsUnknownWhenOwnerTagIsNotFound() {
    LinodeInstance instance = LinodeInstance.createNull();

    assertThat(instance.owner()).isEqualTo("unknown");
  }

  @Test
  void prettyPrintedTagsDoNotShowSpecializedTags() {
    LinodeInstance instance = new LinodeInstance(LinodeId.from(1L), Collections.emptyList(), "", "",
        List.of("auto-created", "owner:principalName"), Collections.emptyList(), null);

    assertThat(instance.prettyPrintTags()).isEqualTo("auto-created");
    assertThat(instance.tags()).containsExactlyInAnyOrder("owner:principalName", "auto-created");
  }
}