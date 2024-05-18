package no.jonasandersen.admin.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import no.jonasandersen.admin.OutputTracker;
import org.junit.jupiter.api.Test;

class DefaultEventPublisherTest {

  @Test
  void publishedEventIsTracked() {
    DefaultEventPublisher publisher = DefaultEventPublisher.createNull();

    OutputTracker<Object> tracker = publisher.track();

    publisher.publishEvent(new Object());

    assertThat(tracker.data()).hasSize(1);
  }
}