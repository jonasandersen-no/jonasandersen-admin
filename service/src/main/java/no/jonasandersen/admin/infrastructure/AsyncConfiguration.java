package no.jonasandersen.admin.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfiguration {

  @Bean
  public TaskExecutor propagatingContextExecutor() {
    SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("async-");
    // decorate task execution with a decorator that supports context propagation aka traceid+spanid
    taskExecutor.setVirtualThreads(true);
    taskExecutor.setTaskDecorator(new ContextPropagatingTaskDecorator());
    return taskExecutor;
  }
}
