package no.jonasandersen.admin.adapter.out.ssh;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RetryerTest {

  private static final Logger log = LoggerFactory.getLogger(RetryerTest.class);

  @Test
  void retryTries3TimesBeforeGivingUp() {

    AtomicInteger times = new AtomicInteger();

    try {
      Retryer.retry(() -> {
        log.info("Running");
        times.getAndIncrement();
        throw new RuntimeException("Fail");
      }, Duration.ZERO);
    } catch (Exception ignored) {
    }

    assertThat(times.get()).isEqualTo(3);
  }

  @Test
  void retryTries5TimesWithNoBackOff() {

    AtomicInteger times = new AtomicInteger();

    try {
      Retryer.retry(() -> {
        log.info("Running");
        times.getAndIncrement();
        throw new RuntimeException("Fail");
      }, 5, Duration.ZERO);
    } catch (Exception ignored) {
    }

    assertThat(times.get()).isEqualTo(5);
  }
}