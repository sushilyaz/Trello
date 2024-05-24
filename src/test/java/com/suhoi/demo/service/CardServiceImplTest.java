package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardMapper;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.service.impl.CardListServiceImpl;
import com.suhoi.demo.service.impl.CardServiceImpl;
import com.suhoi.demo.util.DataUtils;
import com.suhoi.demo.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardListRepository cardListRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    @DisplayName("Test createCard method saves card")
    public void givenCardCreateDto_whenCreateCard_thenSaveCard() {
        // given
        CardCreateDto dto = new CardCreateDto();
        dto.setTitle("Sample Card");
        dto.setDescription("Sample Description");
        dto.setImportance("High");
        dto.setAssignees(List.of("user1", "user2"));
        dto.setDeadline(LocalDate.now());

        CardList cardList = DataUtils.getCardListPersist();
        Card card = DataUtils.getCardPersist();

        BDDMockito.given(cardListRepository.findById(anyLong())).willReturn(Optional.of(cardList));
        BDDMockito.given(cardMapper.map(any(CardCreateDto.class))).willReturn(card);
        BDDMockito.given(cardRepository.save(any(Card.class))).willReturn(card);

        // when
        Card createdCard = cardService.createCard(dto, cardList.getId(), cardList.getBoard().getId());

        // then
        assertEquals(card, createdCard);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    @DisplayName("Test createCard method throws exception if card list not found")
    public void givenCardCreateDto_whenCreateCard_thenThrowException() {
        // given
        CardCreateDto dto = new CardCreateDto();
        dto.setTitle("Sample Card");
        dto.setDescription("Sample Description");
        dto.setImportance("High");
        dto.setAssignees(List.of("user1", "user2"));
        dto.setDeadline(LocalDate.now());

        BDDMockito.given(cardListRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardService.createCard(dto, 1L, 1L));
    }

    @Test
    @DisplayName("Test findCardById method returns CardDto")
    public void givenCardIdAndCardListId_whenFindCardById_thenReturnCardDto() {
        // given
        Card card = DataUtils.getCardPersist();
        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.of(card));
        BDDMockito.given(cardMapper.map(any(Card.class))).willReturn(new CardDto());

        // when
        CardDto cardDto = cardService.findCardById(1L, 1L, 1L);

        // then
        assertNotNull(cardDto);
        verify(cardRepository, times(1)).findCardByIdAndCardListId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Test findCardById method throws exception if card not found")
    public void givenCardIdAndCardListId_whenFindCardById_thenThrowException() {
        // given
        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardService.findCardById(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test findAllCards method returns list of CardDto")
    public void givenCardListId_whenFindAllCards_thenReturnListOfCardDto() {
        // given
        Card card = DataUtils.getCardPersist();
        BDDMockito.given(cardRepository.findCardsByCardListId(anyLong())).willReturn(List.of(card));
        BDDMockito.given(cardMapper.map(any(Card.class))).willReturn(new CardDto());

        // when
        List<CardDto> cardDtos = cardService.findAllCards(1L, 1L);

        // then
        assertFalse(cardDtos.isEmpty());
        verify(cardRepository, times(1)).findCardsByCardListId(anyLong());
    }

    @Test
    @DisplayName("Test update method updates card")
    public void givenCardUpdateDto_whenUpdate_thenUpdateCard() {
        // given
        CardUpdateDto dto = new CardUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));
        dto.setImportance(JsonNullable.of("Medium"));
        dto.setAssignees(JsonNullable.of(List.of("user3", "user4")));
        dto.setDeadline(JsonNullable.of(LocalDate.now().plusDays(1)));

        Card card = DataUtils.getCardPersist();
        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.of(card));
        BDDMockito.doNothing().when(cardMapper).update(any(Card.class), any(CardUpdateDto.class));
        BDDMockito.given(cardRepository.save(any(Card.class))).willReturn(card);
        BDDMockito.given(cardMapper.map(any(Card.class))).willReturn(new CardDto());

        // when
        CardDto updatedCard = cardService.update(dto, 1L, 1L, 1L);

        // then
        assertNotNull(updatedCard);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    @DisplayName("Test update method throws exception if card not found")
    public void givenCardUpdateDto_whenUpdate_thenThrowException() {
        // given
        CardUpdateDto dto = new CardUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));
        dto.setImportance(JsonNullable.of("Medium"));
        dto.setAssignees(JsonNullable.of(List.of("user3", "user4")));
        dto.setDeadline(JsonNullable.of(LocalDate.now().plusDays(1)));

        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardService.update(dto, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test deleteCardById method deletes card")
    public void givenCardIdAndCardListId_whenDeleteCardById_thenDeleteCard() {
        // given
        Card card = DataUtils.getCardPersist();
        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.of(card));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(DataUtils.getJohnPersist());

        // when
        cardService.deleteCardById(1L, 1L, 1L);

        // then
        verify(cardRepository, times(1)).delete(any(Card.class));
    }

    @Test
    @DisplayName("Test deleteCardById method throws exception if card not found")
    public void givenCardIdAndCardListId_whenDeleteCardById_thenThrowException() {
        // given
        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardService.deleteCardById(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test deleteCardById method throws exception if user has no permission")
    public void givenCardIdAndCardListId_whenDeleteCardById_thenThrowAccessDeniedException() {
        // given
        Card card = DataUtils.getCardPersist();
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("jane");
        anotherUser.setEmail("jane@example.com");

        BDDMockito.given(cardRepository.findCardByIdAndCardListId(anyLong(), anyLong())).willReturn(Optional.of(card));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(anotherUser);

        // when / then
        assertThrows(AccessPermissionDeniedException.class, () -> cardService.deleteCardById(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test findBurnedCards method returns list of burned CardDto")
    public void givenCardListId_whenFindBurnedCards_thenReturnListOfBurnedCardDto() {
        // given
        Card card = DataUtils.getCardPersist();
        card.setBurned(true);
        BDDMockito.given(cardRepository.findCardsByCardListIdAndBurned(anyLong(), eq(true))).willReturn(List.of(card));
        BDDMockito.given(cardMapper.map(any(Card.class))).willReturn(new CardDto());

        // when
        List<CardDto> burnedCards = cardService.findBurnedCards(1L, 1L);

        // then
        assertFalse(burnedCards.isEmpty());
        verify(cardRepository, times(1)).findCardsByCardListIdAndBurned(anyLong(), eq(true));
    }
}

