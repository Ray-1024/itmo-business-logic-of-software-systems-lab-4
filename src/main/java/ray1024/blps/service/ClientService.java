package ray1024.blps.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.model.entity.Item;
import ray1024.blps.model.entity.ItemStack;
import ray1024.blps.model.entity.Order;
import ray1024.blps.model.entity.User;
import ray1024.blps.repository.ItemRepository;
import ray1024.blps.repository.OrderRepository;
import ray1024.blps.repository.UserRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private final ItemRepository itemRepository;

    private OrderRepository orderRepository;

    private NotificationService notificationService;

    private UserRepository userRepository;


    @Transactional
    public Order order(@NonNull String username, @NonNull HashSet<ItemStack> itemStacks) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            userRepository.save(User.builder().username(username).password(username).build());
        }
        Order order = orderRepository.save(
                Order.builder()
                        .id(0L)
                        .client(userRepository.findByUsername(username).orElse(null))
                        .packer(null)
                        .courier(null)
                        .status(Order.Status.ORDERED)
                        .items(itemStacks)
                        .creationTime(Instant.now())
                        .build()
        );
        notificationService.save(order.getId(), Order.Status.ORDERED.name());
        return order;
    }

    @Transactional
    public Order addItem(long orderId, long itemId, long count) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElse(Item.builder().build());
        ItemStack itemStack = ItemStack.builder()
                .item(item)
                .count(count)
                .build();
        HashSet<ItemStack> itemStacks = new HashSet<>(order.getItems());
        itemStacks.add(itemStack);
        return orderRepository.save(Order.builder()
                .id(order.getId())
                .client(order.getClient())
                .courier(order.getCourier())
                .packer(order.getPacker())
                .creationTime(order.getCreationTime())
                .items(itemStacks)
                .status(order.getStatus())
                .deliveredTime(order.getDeliveredTime())
                .packedTime(order.getPackedTime())
                .build());
    }
}
