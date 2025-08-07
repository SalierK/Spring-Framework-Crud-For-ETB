package com.codecraft.Crud_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // In a real application, these would be loaded from a database
        // For demo purposes, using BCrypt encoded passwords
        if ("person".equals(username)) {
            return User.withUsername("person")
                    .password(passwordEncoder.encode("personpassword"))
                    .roles("PERSON")
                    .build();
        } else if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("adminpassword"))
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}