package com.example.jobportal.service;

import com.example.jobportal.dto.JwtResponse;
import com.example.jobportal.dto.LoginRequest;
import com.example.jobportal.dto.SignupRequest;
import com.example.jobportal.entity.ERole;
import com.example.jobportal.entity.Role;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.RoleRepository;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtUtils;
import com.example.jobportal.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Transactional
    public String registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        String strRole = signUpRequest.getRole();
        Role userRole;
        if (strRole == null) {
            userRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
                    .orElseThrow(() -> new RuntimeException("User Role not set."));
        } else {
            switch (strRole.toLowerCase()) {
                case "admin":
                    userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Admin role not found"));
                    break;
                case "recruiter":
                    userRole = roleRepository.findByName(ERole.ROLE_RECRUITER)
                            .orElseThrow(() -> new RuntimeException("Recruiter role not found"));
                    break;
                default:
                    userRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
                            .orElseThrow(() -> new RuntimeException("Candidate role not found"));
            }
        }
        user.setRole(userRole);
        userRepository.save(user);
        return "User registered successfully!";
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenFromUsername(loginRequest.getUsername());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().iterator().next().getAuthority());
    }
}