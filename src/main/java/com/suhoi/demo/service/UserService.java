package com.suhoi.demo.service;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.model.User;

public interface UserService {
    User createUser(UserCreateDto dto);
}
