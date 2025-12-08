package com.koushik.expansetracker.security;

import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.repository.security.RoleRepository;
import com.koushik.expansetracker.repository.security.UserRepository;
import com.koushik.expansetracker.repository.security.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        // Fetch all role names assigned to this user
        List<String> roleNames = userRoleRepository.findByUserId(user.getUserId())
                .stream()
                .map(UserRole::getRoleId)
                .map(roleId -> roleRepository.findById(roleId).orElse(null))
                .filter(r -> r != null)
                .map(Role::getRoleName)  // e.g., "ROLE_ADMIN", "ROLE_USER"
                .toList();

        return new CustomUserDetails(user, roleNames);
    }
}
