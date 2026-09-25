package com.example.restaurant.Controller;

import com.example.restaurant.Services.MenuItemService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.MenuItemAvailabilityRequest;
import com.example.restaurant.dto.request.MenuItemRequest;
import com.example.restaurant.dto.response.MenuItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItemResponse> update(@PathVariable Long itemId,
                                                   @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.update(itemId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long itemId) {
        menuItemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable Long itemId) {
        return ResponseEntity.ok(menuItemService.getById(itemId));
    }


    @GetMapping
    public ResponseEntity<PageResponse<MenuItemResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean available,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(menuItemService.search(name, categoryId, available, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{itemId}/availability")
    public ResponseEntity<MenuItemResponse> setAvailability(@PathVariable Long itemId,
                                                            @Valid @RequestBody MenuItemAvailabilityRequest request) {
        return ResponseEntity.ok(menuItemService.setAvailability(itemId, request));
    }
}
