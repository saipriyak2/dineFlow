package com.example.restaurant.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_item_id",nullable=false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MenuItem menuItem;

    @Column(nullable=false)
    private String nameAtOrder;

    @Column(nullable=false,precision = 10,scale=2)
    private BigDecimal priceAtOrder;

    @Column(nullable=false)
    private int quantity;
}

