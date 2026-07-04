package no.jonasandersen.admin.minecraft.internal;

import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MinecraftConfig {

  @Bean
  RecurringTask<Void> syncRecurringTask(SyncTask syncTask) {
    return Tasks.recurring("rsync-andersen-minecraft", FixedDelay.ofHours(1))
        .execute(
            (taskInstance, executionContext) -> {
              syncTask.executeSyncTask(executionContext.getExecution().getExecutionTime());
            });
  }
}
