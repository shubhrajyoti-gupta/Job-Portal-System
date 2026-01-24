package com.example.jobportal.service;

import com.example.jobportal.dto.SignupRequest;
import com.example.jobportal.entity.ERole;
import com.example.jobportal.entity.Role;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.RoleRepository;
import com.example.jobportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
    }

    @Test
    void registerUser_shouldThrowIfUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(signupRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldCreateUserWithDefaultRole() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Role mockRole = new Role(ERole.ROLE_CANDIDATE);
        when(roleRepository.findByName(ERole.ROLE_CANDIDATE)).thenReturn(Optional.of(mockRole));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = authService.registerUser(signupRequest);

        assertEquals("User registered successfully!", result);
        verify(userRepository).save(any(User.class));
    }
}