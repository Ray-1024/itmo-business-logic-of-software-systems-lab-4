package ray1024.blps.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ray1024.blps.model.entity.Shop;
import ray1024.blps.repository.ShopRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public List<Shop> getAll(int pageSize, int pageNumber) {
        return shopRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)).getContent();
    }
}
