package com.suhoi.demo.in.controller;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.model.User;
import com.suhoi.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto userCreateDto,
                                    BindingResult bindingResult,
                                    UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            User user = userService.createUser(userCreateDto);
            return ResponseEntity
                    .created(uriBuilder.path("/api/users/{id}")
                            .buildAndExpand(user.getId()).toUri())
                    .body(user);
        }
    }
}
