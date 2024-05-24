package com.suhoi.demo.util;

import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String name = authentication.getName();
        return userRepository.findByEmail(name).orElseThrow(() -> new DataNotFoundException("User Not Found"));
    }
}
