package com.suhoi.demo.in.controller;

import com.suhoi.demo.dto.AuthDto;
import com.suhoi.demo.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * Контроллер аутентификации пользователя с JWT токеном
     *
     * @param authDto
     * @return
     */
    @PostMapping("/login")
    public String auth(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtUtils.generateToken(authDto.getEmail());
    }
}
