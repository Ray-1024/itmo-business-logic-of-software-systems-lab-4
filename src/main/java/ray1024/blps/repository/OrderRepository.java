package ray1024.blps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ray1024.blps.model.entity.Order;
import ray1024.blps.model.entity.User;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByPacker(User packer);

    Optional<Order> findByCourier(User courier);

    Optional<Order> findFirstByPackerIsNull();

    Optional<Order> findFirstByCourierIsNull();
}
