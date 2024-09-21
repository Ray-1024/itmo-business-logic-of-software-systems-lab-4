package ray1024.blps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ray1024.blps.model.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
