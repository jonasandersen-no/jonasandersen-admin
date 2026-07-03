package no.jonasandersen.admin.minecraft;

import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import com.jcraft.jsch.JSchException;
import no.jonasandersen.admin.domain.ConnectionInfo;
import no.jonasandersen.admin.domain.Feature;
import no.jonasandersen.admin.domain.Hostname;
import no.jonasandersen.admin.domain.PasswordConnectionInfo;
import no.jonasandersen.admin.domain.SensitiveString;
import no.jonasandersen.admin.infrastructure.AdminProperties;
import no.jonasandersen.admin.infrastructure.AdminProperties.Minecraft.Andersen;
import no.jonasandersen.admin.infrastructure.Features;
import no.jonasandersen.admin.ssh.FileExecutor;
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

  @Bean(destroyMethod = "cleanup")
  FileExecutor minecraftFileExecutor(AdminProperties properties) throws JSchException {
    Andersen andersen = properties.minecraft().andersen();

    FileExecutor fileExecutor = FileExecutor.create();

    ConnectionInfo info =
        new PasswordConnectionInfo(
            andersen.username(),
            SensitiveString.of(andersen.password()),
            new Hostname(andersen.hostname()),
            22);
    fileExecutor.setup(info);
    return fileExecutor;
  }
}
