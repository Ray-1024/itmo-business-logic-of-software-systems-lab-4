package ray1024.blps.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.model.entity.Notification;
import ray1024.blps.model.entity.Order;
import ray1024.blps.model.message.OrderStatusChangedMessage;
import ray1024.blps.repository.NotificationRepository;
import ray1024.blps.repository.OrderRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;

    @Value("${topic.order.status.changed}")
    private String orderChangedStatusTopic;

    private final KafkaProducer<String, Object> kafkaProducer;

    @Transactional
    public void save(long orderId, @NonNull String message) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        Notification notification = Notification.builder().user(order.getClient()).message(message).build();
        notificationRepository.save(notification);
    }

    public void sendNotification() {
        Optional<Notification> notification = notificationRepository.findFirstBy(Sort.unsorted());
        if (notification.isEmpty()) {
            log.info("Notification not found");
            return;
        }
                                                                                                                                                        log.info("Dear {}, your order changed status to {}", notification.get().getUser().getUsername(), notification.get().getMessage());
        kafkaProducer.send(new ProducerRecord<>(orderChangedStatusTopic, OrderStatusChangedMessage.builder()
                .orderStatus(notification.get().getMessage())
                .username(notification.get().getUser().getUsername())
                .build()));
        notificationRepository.delete(notification.get());
    }
}
