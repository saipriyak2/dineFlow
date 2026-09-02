package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.UserService;
import com.example.restaurant.dto.request.UserLoginRequest;
import com.example.restaurant.dto.request.UserRegisterRequest;
import com.example.restaurant.dto.request.UserUpdateRequest;
import com.example.restaurant.dto.response.UserResponse;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.Role;
import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.InvalidCredentialsException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.UserMapper;
import com.example.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {
       if(userRepository.existsByEmail(request.email())) {
           throw new DuplicateResourceException("An account with email'" + request.email() + "'already exists");
       }

       User user = User.builder()
               .name(request.name())
               .email(request.email())
               .password(passwordEncoder.encode(request.password()))
               .role(Role.CUSTOMER)
               .build();
       return UserMapper.toResponse(userRepository.save(user));

    }

    @Override
    public UserResponse authenticate(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)

    public UserResponse getById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User",userId));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long userId,UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User",userId));

        if(!user.getEmail().equalsIgnoreCase(request.email())
            && userRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("An account with email'"+ request.email()+"'already exists");
        }

        user.setName(request.name());
        user.setEmail(request.email());

        return UserMapper.toResponse(userRepository.save(user));
    }
}
