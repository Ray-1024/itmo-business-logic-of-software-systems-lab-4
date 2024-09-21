package ray1024.blps.schedule;

import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ray1024.blps.service.NotificationService;

@Component
@AllArgsConstructor
public class NotificationJob implements Job {

    private NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        notificationService.sendNotification();
    }
}
