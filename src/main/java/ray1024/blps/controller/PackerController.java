package ray1024.blps.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ray1024.blps.model.entity.User;
import ray1024.blps.model.response.OrderResponse;
import ray1024.blps.service.PackerService;

@AllArgsConstructor
@RestController
public class PackerController {
    private PackerService packerService;


    @GetMapping("/api/packer/order")
    public OrderResponse findOrderToDelivery() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return OrderResponse.builder()
                .order(packerService.findNewOrder(user))
                .build();
    }


    @PutMapping("/api/packer/order")
    public OrderResponse deliveryOrder() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return OrderResponse.builder()
                .order(packerService.doneOrder(user))
                .build();
    }
}
