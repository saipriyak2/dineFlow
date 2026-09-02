package com.example.restaurant.repository;

import com.example.restaurant.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

    boolean existsByNameIgnoreCase(String name);
}
