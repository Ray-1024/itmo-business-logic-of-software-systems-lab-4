package ray1024.blps.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ray1024.blps.service.NotificationService;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedNotificator implements JavaDelegate {

    private NotificationService notificationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        notificationService.sendNotification();
    }
}
