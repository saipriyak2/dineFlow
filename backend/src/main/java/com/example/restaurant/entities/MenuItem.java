package com.example.restaurant.entities;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private MenuCategory category;
}
