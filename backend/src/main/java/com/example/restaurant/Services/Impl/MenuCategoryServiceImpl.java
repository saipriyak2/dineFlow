package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.MenuCategoryService;
import com.example.restaurant.dto.request.MenuCategoryRequest;
import com.example.restaurant.dto.response.MenuCategoryResponse;
import com.example.restaurant.entity.MenuCategory;
import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.MenuCategoryMapper;
import com.example.restaurant.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MenuCategoryServiceImpl implements MenuCategoryService {
    private final  MenuCategoryRepository menuCategoryRepository;


    @Override
    @Transactional
    public MenuCategoryResponse create(MenuCategoryRequest request) {
        if(menuCategoryRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("A category named'"+request.name()+"'already exists");

        }

        MenuCategory category =  MenuCategory.builder()
                .name(request.name())
                .description(request.description())
                .build();

        return MenuCategoryMapper.toResponse(menuCategoryRepository.save(category));
    }

    @Override
    @Transactional
    public MenuCategoryResponse update(Long categoryId, MenuCategoryRequest request) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Menu category", categoryId));

        if (!category.getName().equalsIgnoreCase(request.name())
                && menuCategoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("A category named '" + request.name() + "' already exists");
        }

        category.setName(request.name());
        category.setDescription(request.description());

        return MenuCategoryMapper.toResponse(menuCategoryRepository.save(category));
    }


    @Override
    @Transactional
    public void delete(Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Menu category",categoryId));

        if(!category.getMenuItems().isEmpty()){
            throw new InvalidOperationException(
                    "Cannot delete category'"+category.getName()+"'while it still has menu items");

        }
        menuCategoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuCategoryResponse getById(Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(()->ResourceNotFoundException.of("Menu category",categoryId));
        return MenuCategoryMapper.toResponse(category);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> getAll() {
        return menuCategoryRepository.findAll().stream()
                .map(MenuCategoryMapper::toResponse)
                .toList();
    }

}
