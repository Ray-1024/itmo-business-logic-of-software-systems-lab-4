package ray1024.blps.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.model.entity.Order;
import ray1024.blps.model.entity.User;
import ray1024.blps.repository.OrderRepository;
import ray1024.blps.repository.UserRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
public class PackerService {

    private final UserRepository userRepository;
    private OrderRepository orderRepository;
    private NotificationService notificationService;

    @Transactional
    public Order findNewOrder(@NonNull String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(User.builder().username(username).password(username).build());
        }
        User user = userRepository.findByUsername(username).orElseThrow();
        if (orderRepository.findByPacker(user).isPresent())
            throw new IllegalStateException("Already have order in packing");
        Order order = orderRepository.findFirstByPackerIsNull().orElseThrow();
        order.setStatus(Order.Status.PACKING);
        order.setPacker(user);
        notificationService.save(order.getId(), Order.Status.PACKING.name());
        return orderRepository.save(order);
    }

    @Transactional
    public Order doneOrder(@NonNull String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(User.builder().username(username).password(username).build());
        }
        User user = userRepository.findByUsername(username).orElseThrow();
        if (orderRepository.findByPacker(user).isEmpty())
            throw new IllegalStateException("No orders to done");
        Order order = orderRepository.findByPacker(user).orElseThrow();
        order.setStatus(Order.Status.PACKED);
        order.setPackedTime(Instant.now());
        notificationService.save(order.getId(), Order.Status.PACKED.name());
        return orderRepository.save(order);
    }
}
