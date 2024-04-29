package com.suhoi.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @Size(min = 4, max = 10)
    private String username;
    @Email
    private String email;
    @Size(min = 4, max = 10)
    private String password;
}
