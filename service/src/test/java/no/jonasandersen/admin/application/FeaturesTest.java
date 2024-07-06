package no.jonasandersen.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.infrastructure.Features;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeaturesTest {

  @BeforeEach
  void setUp() {
    Features.reset();
  }

  @Test
  void featureIsEnabledWhenDefaultIsEnabledAndNoOverrideExists() {
    assertThat(Features.isEnabled(Feature.LINODE_STUB)).isTrue();
  }

  @Test
  void featureIsDisabledWhenDefaultIsEnabledAndOverrideExists() {
    Features.setFeature(Feature.LINODE_STUB, false);
    assertThat(Features.isEnabled(Feature.LINODE_STUB)).isFalse();
  }
}