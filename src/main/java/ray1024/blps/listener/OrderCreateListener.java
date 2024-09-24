package ray1024.blps.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import ray1024.blps.model.entity.Order;
import ray1024.blps.service.ClientService;

import java.util.HashSet;

@Service
@Slf4j
@AllArgsConstructor
public class OrderCreateListener implements JavaDelegate {
    private ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String initiator = (String) delegateExecution.getVariable("initiator");
        Order order = clientService.order(initiator, new HashSet<>());
        delegateExecution.setVariable("orderId", order.getId());
    }
}
