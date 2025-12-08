package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.SignupRequest;
import com.koushik.expansetracker.dto.SignupResponse;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.repository.security.UserRepository;
import com.koushik.expansetracker.repository.security.RoleRepository;
import com.koushik.expansetracker.repository.security.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new SignupResponse("Email already exists!");
        }

        // Create User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Assign Default Role = ROLE_USER
        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        UserRole mapping = new UserRole();
        mapping.setUserId(user.getUserId());
        mapping.setRoleId(defaultRole.getRoleId());
        userRoleRepository.save(mapping);

        return new SignupResponse("Signup successful! Please login.");
    }
}
