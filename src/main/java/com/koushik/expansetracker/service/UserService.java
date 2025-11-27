package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.CreateUserRequest;
import com.koushik.expansetracker.entity.User;
import com.koushik.expansetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // secure!
                .isActive(true)
                .build();

        return userRepository.save(user);
    }
}
