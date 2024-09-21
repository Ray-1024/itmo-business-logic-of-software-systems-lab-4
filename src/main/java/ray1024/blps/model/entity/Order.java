package ray1024.blps.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "packer_id", referencedColumnName = "id")
    @JsonIgnore
    private User packer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    @JsonIgnore
    private User courier;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    private Instant creationTime;
    @JsonIgnore
    private Instant packedTime;
    @JsonIgnore
    private Instant deliveredTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "order_itemstack", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "itemstack_id"))
    private Set<ItemStack> items = new HashSet<>();

    public enum Status {
        ORDERED, PACKING, PACKED, DELIVERING, DELIVERED
    }
}
