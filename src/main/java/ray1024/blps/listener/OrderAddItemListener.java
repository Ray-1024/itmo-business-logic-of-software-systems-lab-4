package ray1024.blps.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ray1024.blps.service.ClientService;

@Service
@Slf4j
@AllArgsConstructor
public class OrderAddItemListener implements JavaDelegate {
    private ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long orderId = (Long) delegateExecution.getVariable("orderId");
        Long itemId = (Long) delegateExecution.getVariable("itemId");
        Long count = (Long) delegateExecution.getVariable("itemCount");
        clientService.addItem(orderId, itemId, count);
    }
}
