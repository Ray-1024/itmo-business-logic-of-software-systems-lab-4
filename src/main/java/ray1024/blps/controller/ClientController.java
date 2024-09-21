package ray1024.blps.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ray1024.blps.model.entity.User;
import ray1024.blps.model.request.OrderRequest;
import ray1024.blps.model.response.OrderResponse;
import ray1024.blps.service.ClientService;

@AllArgsConstructor
@RestController
public class ClientController {
    private final ClientService clientService;


    @PostMapping("/api/client/order")
    public OrderResponse order(@RequestBody OrderRequest order) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return OrderResponse.builder()
                .order(clientService.order(user, order.getItems()))
                .build();
    }
}
