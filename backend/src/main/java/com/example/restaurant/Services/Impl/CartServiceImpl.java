package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.CartService;
import com.example.restaurant.dto.request.AddCartItemRequest;
import com.example.restaurant.dto.request.UpdateCartItemRequest;
import com.example.restaurant.dto.response.CartResponse;
import com.example.restaurant.entity.Cart;
import com.example.restaurant.entity.CartItem;
import com.example.restaurant.entity.MenuItem;
import com.example.restaurant.entity.User;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.CartMapper;
import com.example.restaurant.repository.CartRepository;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.dnd.InvalidDnDOperationException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    private Cart getOrCreatecart(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> ResourceNotFoundException.of("User",userId));
                    Cart cart = Cart.builder().user(user).build();
                    return cartRepository.save(cart);
                });
    }


    @Override
    @Transactional
    public CartResponse getCartForUser(Long userId){
        return CartMapper.toResponse(getOrCreatecart(userId));
    }

    @Override
    @Transactional
    public CartResponse addItem(Long userId, AddCartItemRequest request){
        Cart cart = getOrCreatecart(userId);

        MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
                .orElseThrow(() -> ResourceNotFoundException.of("Menu item",request.menuItemId()));

        if(!menuItem.isAvailable()){
            throw new InvalidOperationException("'"+menuItem.getName()+"'is currently unavailable");
        }

        cart.getItems().stream()
                .filter(existing -> existing.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setQuantity(existing.getQuantity()+request.quantity()),
                        () -> cart.getItems().add(CartItem.builder()
                                .cart(cart)
                                .menuItem(menuItem)
                                .quantity(request.quantity())
                                .build()
                ));
                        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long cartItemId,UpdateCartItemRequest request){
        Cart cart = getOrCreatecart(userId);

        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> ResourceNotFoundException.of("Cart item",cartItemId));

        if(request.quantity()<=0){
            cart.getItems().remove(item);
        }else{
            item.setQuantity(request.quantity());
        }
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long userId, Long cartItemId){
        Cart cart = getOrCreatecart(userId);

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        if(!removed){
            throw ResourceNotFoundException.of("Cart item",cartItemId);

        }
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(Long userId){
        Cart cart = getOrCreatecart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
