package com.orbit.user.service;

import com.orbit.user.dto.*;
import com.orbit.user.model.User;
import com.orbit.user.repository.UserRepository;
import com.orbit.user.security.JwtService;
import com.exceptions.orbit.exception.UserNotFoundException;
import com.exceptions.orbit.exception.EmailAlreadyExistsException;
import com.exceptions.orbit.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        log.info("Attempting to register user with email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id={}", savedUser.getId());

        return mapToResponse(savedUser);
    }

    @Override
    public AuthResponse loginUser(UserLoginRequest request) {
        log.info("Login attempt for email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found for email={}", request.getEmail());
                    return new UserNotFoundException(request.getEmail());
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid login attempt for email={}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("userId", String.valueOf(user.getId()));

        String jwtToken = jwtService.generateToken(extraClaims, user.getEmail());
        log.info("Login successful for email={}", request.getEmail());

        return new AuthResponse(jwtToken, "Login Successful");
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id={}", id);
                    return new UserNotFoundException(id);
                });

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
            log.debug("Updated fullName for userId={}: {}", id, request.getFullName());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            log.debug("Password updated for userId={}", id);
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user.setPhone(request.getPhone());
            log.debug("Updated phone for userId={}: {}", id, request.getPhone());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id={}", id);

        return mapToResponse(updatedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("Fetching user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id={}", id);
                    return new UserNotFoundException(id);
                });

        log.info("User retrieved successfully with id={}", id);
        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id={}", id);

        if (!userRepository.existsById(id)) {
            log.warn("User not found for deletion, id={}", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id={}", id);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .isVerified(user.isVerified())
                .build();
    }
}
