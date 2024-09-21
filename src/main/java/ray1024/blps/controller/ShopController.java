package ray1024.blps.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ray1024.blps.model.response.ShopsResponse;
import ray1024.blps.service.ShopService;

@AllArgsConstructor
@RestController
public class ShopController {
    private ShopService shopService;

    @GetMapping("/api/shops")
    public ShopsResponse getAllPaginated(@RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return ShopsResponse.builder().shops(shopService.getAll(pageSize, pageNumber)).build();
    }
}
