package com.suhoi.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {
    @Size(min = 4, max = 10)
    private String username;
    @Email
    private String email;
    @Size(min = 4, max = 10)
    private String password;
}
