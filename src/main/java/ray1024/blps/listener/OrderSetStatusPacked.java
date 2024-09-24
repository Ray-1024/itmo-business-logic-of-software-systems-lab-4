package ray1024.blps.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ray1024.blps.service.PackerService;

@Service
@Slf4j
@AllArgsConstructor
public class OrderSetStatusPacked implements JavaDelegate {
    private PackerService packerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String initiator = (String) delegateExecution.getVariable("initiator");
        packerService.doneOrder(initiator);
    }
}
