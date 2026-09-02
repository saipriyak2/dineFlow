package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.MenuItemService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.MenuItemAvailabilityRequest;
import com.example.restaurant.dto.request.MenuItemRequest;
import com.example.restaurant.dto.response.MenuItemResponse;
import com.example.restaurant.entity.MenuCategory;
import com.example.restaurant.entity.MenuItem;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.MenuItemMapper;
import com.example.restaurant.repository.MenuCategoryRepository;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.MenuItemSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    @Override
@Transactional
public MenuItemResponse create(MenuItemRequest request){
        MenuCategory category = menuCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Menu category",request.categoryId()));

        MenuItem item = MenuItem.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .category(category)
                .build();

        return MenuItemMapper.toResponse(menuItemRepository.save(item));

    }

    @Override
    @Transactional
    public MenuItemResponse update(Long itemId,MenuItemRequest request){
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> ResourceNotFoundException.of("Menu item",itemId));

        MenuCategory category = menuCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Menu category",request.categoryId()));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setImageUrl(request.imageUrl());
        item.setCategory(category);

        return MenuItemMapper.toResponse(menuItemRepository.save(item));
    }
    @Override
    @Transactional
    public void delete(Long itemId) {

        if (!menuItemRepository.existsById(itemId)) {
            throw ResourceNotFoundException.of("Menu item", itemId);
        }

        menuItemRepository.deleteById(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getById(Long itemId){
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> ResourceNotFoundException.of("Menu item",itemId));
        return MenuItemMapper.toResponse(item);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuItemResponse> search(String name,Long categoryId,Boolean available,Pageable pageable){
        Specification<MenuItem> spec = (root, query, criteriaBuilder) -> null;

        Specification<MenuItem> nameSpec = MenuItemSpecifications.nameContains(name);
        if(nameSpec != null){
            spec = spec.and(nameSpec);
        }

        Specification<MenuItem> categorySpec = MenuItemSpecifications.inCategory(categoryId);
        if(categorySpec != null){
            spec = spec.and(categorySpec);
        }

        if(Boolean.TRUE.equals(available)){
            spec = spec.and(MenuItemSpecifications.available());
        }

        Page<MenuItem> page = menuItemRepository.findAll(spec, pageable);
        return PageResponse.of(page,page.getContent().stream().map(MenuItemMapper::toResponse).toList());

    }

    @Override
    @Transactional
    public MenuItemResponse setAvailability(
            Long itemId,
            MenuItemAvailabilityRequest request) {

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() ->
                        ResourceNotFoundException.of("Menu item", itemId));

        item.setAvailable(request.available());

        return MenuItemMapper.toResponse(
                menuItemRepository.save(item)
        );
    }

}

