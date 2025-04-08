package no.jonasandersen.admin.adapter.out.ssh;

import java.time.Duration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

public class Retryer {

  public static final int MAX_ATTEMPTS = 3;
  public static final Duration BACKOFF = Duration.ofSeconds(1);

  public static void retry(Runnable runnable) {
    retry(runnable, MAX_ATTEMPTS, BACKOFF);
  }

  public static void retry(Runnable runnable, int maxAttempts) {
    retry(runnable, maxAttempts, BACKOFF);
  }

  public static void retry(Runnable runnable, Duration backoff) {
    retry(runnable, MAX_ATTEMPTS, backoff);
  }

  public static void retry(Runnable runnable, int maxAttempts, Duration backoff) {
    RetryTemplateBuilder builder = RetryTemplate.builder().maxAttempts(maxAttempts);

    if (backoff.toMillis() > 0) {
      builder.fixedBackoff(backoff);
    }
    builder
        .build()
        .execute(
            context -> {
              runnable.run();
              return null;
            });
  }
}
