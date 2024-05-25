package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.service.BoardService;
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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JsonNullableModule());
        token = jwt().jwt(builder -> builder.subject(DataUtils.getJohnPersist().getEmail()));
    }

    @Test
    @DisplayName("Test create board functionality")
    public void givenBoard_whenCreateBoard_thenSuccessResponse() throws Exception {
        //given
        Board board = DataUtils.getBoardPersist();
        BoardCreateDto dto = BoardCreateDto.builder()
                .title("Sample Board")
                .description("Sample Description")
                .moderators(List.of("moderator1", "moderator2"))
                .members(List.of("member1", "member2"))
                .build();
        BDDMockito.given(boardService.create(any(BoardCreateDto.class))).willReturn(board);

        //when
        ResultActions result = mockMvc.perform(post("/api/boards")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(board)));
    }

    @Test
    @DisplayName("Test get board by id functionality")
    public void givenBoardId_whenGetBoard_thenSuccessResponse() throws Exception {
        //given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("Sample Board");
        boardDto.setDescription("Sample Description");
        boardDto.setCreator("John");
        BDDMockito.given(boardService.findById(anyLong())).willReturn(boardDto);

        //when
        ResultActions result = mockMvc.perform(get("/api/boards/{id}", 1L)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(boardDto)));
    }

    @Test
    @DisplayName("Test get all boards functionality")
    public void givenBoards_whenGetAllBoards_thenSuccessResponse() throws Exception {
        //given
        List<BoardDto> boardDtos = List.of(
                new BoardDto("Board1", "Description1", List.of("mod1"), List.of("mem1"), "creator1"),
                new BoardDto("Board2", "Description2", List.of("mod2"), List.of("mem2"), "creator2")
        );
        BDDMockito.given(boardService.getAll()).willReturn(boardDtos);

        //when
        ResultActions result = mockMvc.perform(get("/api/boards")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(boardDtos)));
    }

    @Test
    @DisplayName("Test delete board by id functionality")
    public void givenBoardId_whenDeleteBoard_thenNoContentResponse() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/api/boards/{id}", 1L)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test update board functionality")
    public void givenBoardUpdateDto_whenUpdateBoard_thenSuccessResponse() throws Exception {
        //given

        BoardUpdateDto updateDto = new BoardUpdateDto(
                JsonNullable.of("Updated Board Title"),
                JsonNullable.of("Updated Description")
        );
        BoardDto updatedBoardDto = new BoardDto("Updated Board Title", "Updated Description", List.of("mod1"), List.of("mem1"), "creator1");
        BDDMockito.given(boardService.update(any(BoardUpdateDto.class), anyLong())).willReturn(updatedBoardDto);

        //when
        ResultActions result = mockMvc.perform(patch("/api/boards/{id}", 1L)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updatedBoardDto)));
    }
}

