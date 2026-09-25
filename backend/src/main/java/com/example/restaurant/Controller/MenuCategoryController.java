package com.example.restaurant.Controller;

import com.example.restaurant.Services.MenuCategoryService;
import com.example.restaurant.dto.request.MenuCategoryRequest;
import com.example.restaurant.dto.response.MenuCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MenuCategoryResponse> create(@Valid @RequestBody MenuCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuCategoryService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<MenuCategoryResponse> update(@PathVariable Long categoryId,
                                                       @Valid @RequestBody MenuCategoryRequest request) {
        return ResponseEntity.ok(menuCategoryService.update(categoryId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        menuCategoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<MenuCategoryResponse> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuCategoryService.getById(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<MenuCategoryResponse>> getAll() {
        return ResponseEntity.ok(menuCategoryService.getAll());
    }
}
