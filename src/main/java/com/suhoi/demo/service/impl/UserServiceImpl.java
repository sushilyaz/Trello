package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.mapper.UserMapper;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserCreateDto dto) {
        User user = userMapper.map(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }
}
