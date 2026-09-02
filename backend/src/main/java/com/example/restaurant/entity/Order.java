package com.example.restaurant.entity;

import com.example.restaurant.entity.enums.FulfillmentMethod;
import com.example.restaurant.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "order" is a reserved SQL keyword, so the table is named "orders"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private FulfillmentMethod fulfillmentMethod;

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal totalAmount;

    @Column(nullable=false)
    @Builder.Default
    private boolean paid = false;

    @Column(nullable=false,updatable=false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderItem> items = new ArrayList<>();


}


