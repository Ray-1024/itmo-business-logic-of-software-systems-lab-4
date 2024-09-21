package ray1024.blps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ray1024.blps.model.entity.ItemStack;

@Repository
public interface ItemStackRepository extends JpaRepository<ItemStack, Long> {
}
