package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.exception.DataAlreadyExistException;
import com.suhoi.demo.mapper.UserMapper;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserCreateDto dto) {
        Optional<User> existUser = userRepository.findByEmail(dto.getEmail());
        if (existUser.isPresent()) {
            throw new DataAlreadyExistException("User with email " + dto.getEmail() + " already exists");
        }
        User user = userMapper.map(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }
}
