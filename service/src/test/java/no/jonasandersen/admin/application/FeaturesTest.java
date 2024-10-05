package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import fixtures.ResetFeatures;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.infrastructure.Features;
import org.junit.jupiter.api.Test;

@ResetFeatures
class FeaturesTest {

  @Test
  void featureIsEnabledWhenDefaultIsEnabledAndNoOverrideExists() {
    assertThat(Features.isEnabled(Feature.LINODE_STUB)).isTrue();
  }

  @Test
  void featureIsDisabledWhenDefaultIsEnabledAndOverrideExists() {
    Features.setFeature(Feature.LINODE_STUB, false);
    assertThat(Features.isEnabled(Feature.LINODE_STUB)).isFalse();
  }

  @Test
  void listAllFeatures() {
    assertThat(Features.isEnabled(Feature.LINODE_STUB)).isTrue();
    Features.setFeature(Feature.LINODE_STUB, false);
    assertThat(Features.getAllFeatures()).hasSize(1);
  }
}