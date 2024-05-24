package com.suhoi.demo.service;

import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.exception.DataAlreadyExistException;
import com.suhoi.demo.mapper.UserMapper;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.impl.UserServiceImpl;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test findByEmail thrown exception because user with this email already exist")
    public void givenUserCreateDto_whenFindById_thenExceptionThrown() {
        //given
        User existUser = DataUtils.getJohnPersist();
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(Optional.of(existUser));
        //when
        assertThrows(
                DataAlreadyExistException.class, () -> userService.createUser(UserCreateDto.builder()
                        .username("suhoi")
                        .email(existUser.getEmail())
                        .password("password")
                        .build())
        );
        //then
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test findByEmail thrown exception because user with this email already exist")
    public void givenUserCreateDto_whenFindById_thenSaveUser() {
        //given
        User existUser = DataUtils.getJohnPersist();
        UserCreateDto build = UserCreateDto.builder()
                .username("suhoi")
                .email(existUser.getEmail())
                .password("password")
                .build();
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        BDDMockito.given(userRepository.save(any(User.class))).willReturn(existUser);
        BDDMockito.given(userMapper.map(any(UserCreateDto.class))).willReturn(existUser);
        BDDMockito.given(passwordEncoder.encode(build.getPassword())).willReturn(anyString());
        //when
        userService.createUser(build);
        //then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
