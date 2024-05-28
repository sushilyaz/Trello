package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhoi.demo.dto.*;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.service.CardService;
import com.suhoi.demo.service.TaskService;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject(DataUtils.getJohnPersist().getEmail()));
        objectMapper.registerModule(new JsonNullableModule());
    }

    @Test
    @DisplayName("Test create task functionality")
    public void givenTask_whenCreateTask_thenSuccessResponse() throws Exception {
        // given
        Task entity = DataUtils.getTaskPersist();
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("New Task");
        dto.setDescription("Task Description");

        BDDMockito.given(taskService.create(any(TaskCreateDto.class), anyLong(), anyLong(), anyLong())).willReturn(entity);

        // when
        ResultActions result = mockMvc.perform(post("/api/boards/1/card-lists/1/cards/1/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(entity)));
    }

    @Test
    @DisplayName("Test get task by ID functionality")
    public void givenTaskId_whenGetTask_thenSuccessResponse() throws Exception {
        // given
        TaskDto taskDto = DataUtils.getTaskDto();
        BDDMockito.given(taskService.findById(anyLong(), anyLong(), anyLong())).willReturn(taskDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1/cards/1/tasks/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(taskDto)));
    }

    @Test
    @DisplayName("Test get all tasks by card ID functionality")
    public void givenCardId_whenGetTasks_thenSuccessResponse() throws Exception {
        // given
        List<TaskDto> taskDtos = Collections.singletonList(DataUtils.getTaskDto());
        BDDMockito.given(taskService.findAllByCard(anyLong(), anyLong(), anyLong())).willReturn(taskDtos);

        // when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1/cards/1/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(taskDtos)));
    }

    @Test
    @DisplayName("Test complete task functionality")
    public void givenTaskId_whenCompleteTask_thenSuccessResponse() throws Exception {
        // given
        TaskDto taskDto = DataUtils.getTaskDto();
        BDDMockito.given(taskService.update(anyLong(), anyLong(), anyLong(), anyLong())).willReturn(taskDto);

        // when
        ResultActions result = mockMvc.perform(patch("/api/boards/1/card-lists/1/cards/1/tasks/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(taskDto)));
    }

    @Test
    @DisplayName("Test delete task functionality")
    public void givenTaskId_whenDeleteTask_thenSuccessResponse() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/api/boards/1/card-lists/1/cards/1/tasks/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}


