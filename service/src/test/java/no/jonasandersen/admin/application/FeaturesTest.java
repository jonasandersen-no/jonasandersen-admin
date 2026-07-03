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
    assertThat(Features.isEnabled(Feature.STUB)).isTrue();
  }

  @Test
  void featureIsDisabledWhenDefaultIsEnabledAndOverrideExists() {
    Features.setFeature(Feature.STUB, false);
    assertThat(Features.isEnabled(Feature.STUB)).isFalse();
  }

  @Test
  void listAllFeatures() {
    assertThat(Features.isEnabled(Feature.STUB)).isTrue();
    Features.setFeature(Feature.STUB, false);
    assertThat(Features.getAllFeatures()).hasSize(1);
  }
}