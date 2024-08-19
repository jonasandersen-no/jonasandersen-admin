package no.jonasandersen.admin.infrastructure;

import com.github.kagkarlsson.scheduler.boot.config.DbSchedulerCustomizer;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TasksConfiguration {

  @Bean
  DbSchedulerCustomizer dbSchedulerCustomizer() {
    return new DbSchedulerCustomizer() {
      @Override
      public Optional<ExecutorService> executorService() {
        return Optional.of(Executors.newVirtualThreadPerTaskExecutor());
      }
    };
  }
}
