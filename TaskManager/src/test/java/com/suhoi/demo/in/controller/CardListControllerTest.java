package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhoi.demo.dto.*;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.service.BoardService;
import com.suhoi.demo.service.CardListService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(CardListController.class)
public class CardListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardListService cardListService;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JsonNullableModule());
        token = jwt().jwt(builder -> builder.subject(DataUtils.getJohnPersist().getEmail()));
    }

    @Test
    @DisplayName("Test create card list functionality")
    public void givenCardList_whenCreateCardList_thenSuccessResponse() throws Exception {
        //given
        CardList entity = DataUtils.getCardListPersist();
        CardListCreateDto dto = new CardListCreateDto();
        dto.setTitle("New CardList");
        dto.setDescription("CardList Description");

        BDDMockito.given(cardListService.createCardList(any(CardListCreateDto.class), anyLong())).willReturn(entity);

        //when
        ResultActions result = mockMvc.perform(post("/api/boards/1/card-lists")
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
    @DisplayName("Test get card lists by board id")
    public void givenBoardId_whenGetCardLists_thenReturnCardListDtos() throws Exception {
        //given
        CardListDto cardListDto = new CardListDto();
        cardListDto.setTitle("CardList Title");
        cardListDto.setDescription("CardList Description");
        cardListDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        cardListDto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        List<CardListDto> cardListDtos = List.of(cardListDto);

        BDDMockito.given(cardListService.findCardListByBoardId(anyLong())).willReturn(cardListDtos);

        //when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardListDtos)));
    }

    @Test
    @DisplayName("Test get card list by id")
    public void givenBoardIdAndCardListId_whenGetCardList_thenReturnCardListDto() throws Exception {
        //given
        CardListDto cardListDto = new CardListDto();
        cardListDto.setTitle("CardList Title");
        cardListDto.setDescription("CardList Description");
        cardListDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        cardListDto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        BDDMockito.given(cardListService.findCardListById(anyLong(), anyLong())).willReturn(cardListDto);

        //when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardListDto)));
    }

    @Test
    @DisplayName("Test update card list functionality")
    public void givenCardListUpdateDto_whenUpdateCardList_thenSuccessResponse() throws Exception {
        //given
        CardListDto cardListDto = new CardListDto();
        cardListDto.setTitle("CardList Title");
        cardListDto.setDescription("CardList Description");
        cardListDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        cardListDto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        CardListUpdateDto dto = new CardListUpdateDto(
                JsonNullable.of("Updated Title"),
                JsonNullable.of("Updated Description")
        );

        BDDMockito.given(cardListService.update(any(CardListUpdateDto.class), anyLong(), anyLong())).willReturn(cardListDto);

        //when
        ResultActions result = mockMvc.perform(patch("/api/boards/1/card-lists/{id}", 1L)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardListDto)));
    }

    @Test
    @DisplayName("Test delete card list functionality")
    public void givenBoardIdAndCardListId_whenDeleteCardList_thenSuccessResponse() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/api/boards/1/card-lists/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

