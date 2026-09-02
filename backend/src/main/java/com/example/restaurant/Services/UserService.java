package com.example.restaurant.Services;

import com.example.restaurant.dto.request.UserLoginRequest;
import com.example.restaurant.dto.request.UserRegisterRequest;
import com.example.restaurant.dto.request.UserUpdateRequest;
import com.example.restaurant.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse authenticate(UserLoginRequest request);
    UserResponse getById(Long userId);
    UserResponse updateProfile(Long userId, UserUpdateRequest request);
}
