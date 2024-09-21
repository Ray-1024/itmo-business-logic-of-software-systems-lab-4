package ray1024.blps.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ray1024.blps.model.message.OrderStatusChangedMessage;

@Component
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedHandler {
    @KafkaListener(topics = "${topic.order.status.changed}", groupId = "default", containerFactory = "kafkaListenerFactory")
    public void handleOrderStatus(
            @Payload OrderStatusChangedMessage message
    ) {
        log.info("Dear {}, your order changed status to {}", message.getUsername(), message.getOrderStatus());
    }
}
