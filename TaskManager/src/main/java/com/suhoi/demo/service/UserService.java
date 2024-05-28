package com.suhoi.demo.service;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.model.User;

public interface UserService {
    /**
     * Создание нового пользователя
     *
     * @param dto
     * @return
     */
    User createUser(UserCreateDto dto);
}
