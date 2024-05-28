package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhoi.demo.dto.UserCreateDto;
import com.suhoi.demo.model.User;
import com.suhoi.demo.service.UserService;
import com.suhoi.demo.util.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject(DataUtils.getJohnPersist().getEmail()));
    }

    @Test
    @DisplayName("Test create user functionality")
    public void givenUser_whenCreateUser_thenSuccessResponse() throws Exception {
        //given
        User entity = DataUtils.getJohnPersist();
        UserCreateDto dto = UserCreateDto.builder()
                .username("Johns")
                .email("john@gmail.com")
                .password("password1")
                .build();
        BDDMockito.given(userService.createUser(any(UserCreateDto.class))).willReturn(entity);
        //when
        ResultActions result = mockMvc.perform(post("/api/register")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(entity)));
    }

    @Test
    @DisplayName("Test create user failed, because email is not valid functionality")
    public void givenUser_whenCreateUser_thenFailedResponse() throws Exception {
        //given
        UserCreateDto dto = UserCreateDto.builder()
                .username("Johns")
                .email("johngmail.com")
                .password("password1")
                .build();
        //when
        ResultActions result = mockMvc.perform(post("/api/register")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Data no valid")));
    }
}
