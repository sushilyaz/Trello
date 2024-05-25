package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhoi.demo.dto.*;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.service.CardService;
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

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardService cardService;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject(DataUtils.getJohnPersist().getEmail()));
        objectMapper.registerModule(new JsonNullableModule());
    }

    @Test
    @DisplayName("Test create card functionality")
    public void givenCard_whenCreateCard_thenSuccessResponse() throws Exception {
        // given
        Card entity = DataUtils.getCardPersist();
        CardCreateDto dto = new CardCreateDto();
        dto.setTitle("New Card");
        dto.setDescription("Card Description");
        dto.setImportance("High");
        dto.setAssignees(Collections.emptyList());
        dto.setDeadline(LocalDate.now());

        BDDMockito.given(cardService.createCard(any(CardCreateDto.class), anyLong(), anyLong())).willReturn(entity);

        // when
        ResultActions result = mockMvc.perform(post("/api/boards/1/card-lists/1/cards")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(entity)));
    }

    @Test
    @DisplayName("Test get all cards functionality")
    public void givenCardListId_whenGetCards_thenSuccessResponse() throws Exception {
        // given

        List<CardDto> cardDtos = Collections.singletonList(DataUtils.getCardDto());
        BDDMockito.given(cardService.findAllCards(anyLong(), anyLong())).willReturn(cardDtos);

        // when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1/cards")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardDtos)));
    }

    @Test
    @DisplayName("Test get card by ID functionality")
    public void givenCardId_whenGetCard_thenSuccessResponse() throws Exception {
        // given
        CardDto cardDto = DataUtils.getCardDto();
        BDDMockito.given(cardService.findCardById(anyLong(), anyLong(), anyLong())).willReturn(cardDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1/cards/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardDto)));
    }

    @Test
    @DisplayName("Test delete card functionality")
    public void givenCardId_whenDeleteCard_thenSuccessResponse() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/api/boards/1/card-lists/1/cards/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test update card functionality")
    public void givenCardId_whenUpdateCard_thenSuccessResponse() throws Exception {
        // given
        CardDto cardDto = DataUtils.getCardDto();
        CardUpdateDto updateDto = new CardUpdateDto();
        updateDto.setTitle(JsonNullable.of("Updated Title"));
        updateDto.setDescription(JsonNullable.of("Updated Description"));
        updateDto.setImportance(JsonNullable.of("Medium"));
        updateDto.setAssignees(JsonNullable.of(Collections.singletonList("user1")));
        updateDto.setDeadline(JsonNullable.of(LocalDate.now().plusDays(1)));

        BDDMockito.given(cardService.update(any(CardUpdateDto.class), anyLong(), anyLong(), anyLong())).willReturn(cardDto);

        // when
        ResultActions result = mockMvc.perform(patch("/api/boards/1/card-lists/1/cards/1")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cardDto)));
    }

    @Test
    @DisplayName("Test get burned cards functionality")
    public void givenCardListId_whenGetBurnedCards_thenSuccessResponse() throws Exception {
        // given
        List<CardDto> burnedCards = Collections.singletonList(DataUtils.getCardDto());
        BDDMockito.given(cardService.findBurnedCards(anyLong(), anyLong())).willReturn(burnedCards);

        // when
        ResultActions result = mockMvc.perform(get("/api/boards/1/card-lists/1/cards/burned-cards")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(burnedCards)));
    }
}

