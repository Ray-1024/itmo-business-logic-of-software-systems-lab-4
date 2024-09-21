package ray1024.blps.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.model.entity.Order;
import ray1024.blps.model.entity.User;
import ray1024.blps.repository.OrderRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CourierService {

    private OrderRepository orderRepository;
    private NotificationService notificationService;

    @Transactional
    public Order findNewOrder(@NonNull User user) {
        if (orderRepository.findByCourier(user).isPresent())
            throw new IllegalStateException("Already have order in packing");
        Order order = orderRepository.findFirstByCourierIsNull().orElseThrow();
        order.setStatus(Order.Status.DELIVERING);
        notificationService.save(order.getId(), Order.Status.DELIVERING.name());
        order.setCourier(user);
        return orderRepository.save(order);
    }

    @Transactional
    public Order doneOrder(@NonNull User user) {
        if (orderRepository.findByCourier(user).isEmpty())
            throw new IllegalStateException("No orders to done");
        Order order = orderRepository.findByCourier(user).orElseThrow();
        order.setStatus(Order.Status.DELIVERED);
        order.setDeliveredTime(Instant.now());
        notificationService.save(order.getId(), Order.Status.DELIVERED.name());
        return orderRepository.save(order);
    }
}
