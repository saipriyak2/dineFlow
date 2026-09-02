package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false,unique=true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @Column(nullable=false)
    private int rating;

    @Column(length=2000)
    private String text;

    @Column(nullable=false,updatable=false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}


