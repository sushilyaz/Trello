package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardListMapper;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.service.impl.CardListServiceImpl;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardListServiceImplTest {

    @Mock
    private CardListRepository cardListRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CardListMapper cardListMapper;

    @InjectMocks
    private CardListServiceImpl cardListService;

    @Test
    @DisplayName("Test createCardList method saves card list")
    public void givenCardListCreateDto_whenCreateCardList_thenSaveCardList() {
        // given
        CardListCreateDto dto = new CardListCreateDto();
        dto.setTitle("Sample CardList");
        dto.setDescription("Sample Description");

        Board board = DataUtils.getBoardPersist();
        CardList cardList = DataUtils.getCardListPersist();

        BDDMockito.given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        BDDMockito.given(cardListMapper.map(any(CardListCreateDto.class))).willReturn(cardList);
        BDDMockito.given(cardListRepository.save(any(CardList.class))).willReturn(cardList);

        // when
        CardList createdCardList = cardListService.createCardList(dto, board.getId());

        // then
        assertEquals(cardList, createdCardList);
        verify(cardListRepository, times(1)).save(any(CardList.class));
    }

    @Test
    @DisplayName("Test createCardList method throws exception if board not found")
    public void givenCardListCreateDto_whenCreateCardList_thenThrowException() {
        // given
        CardListCreateDto dto = new CardListCreateDto();
        dto.setTitle("Sample CardList");
        dto.setDescription("Sample Description");

        BDDMockito.given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardListService.createCardList(dto, 1L));
    }

    @Test
    @DisplayName("Test findCardListByBoardId method returns list of CardListDto")
    public void givenBoardId_whenFindCardListByBoardId_thenReturnListOfCardListDto() {
        // given
        CardList cardList = DataUtils.getCardListPersist();
        BDDMockito.given(cardListRepository.findByBoardId(anyLong())).willReturn(List.of(cardList));
        BDDMockito.given(cardListMapper.map(any(CardList.class))).willReturn(new CardListDto());

        // when
        List<CardListDto> cardListDtos = cardListService.findCardListByBoardId(1L);

        // then
        assertFalse(cardListDtos.isEmpty());
        verify(cardListRepository, times(1)).findByBoardId(anyLong());
    }

    @Test
    @DisplayName("Test findCardListById method throws exception if card list not found")
    public void givenBoardIdAndCardListId_whenFindCardListById_thenThrowException() {
        // given
        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardListService.findCardListById(1L, 1L));
    }

    @Test
    @DisplayName("Test findCardListById method returns CardListDto")
    public void givenBoardIdAndCardListId_whenFindCardListById_thenReturnCardListDto() {
        // given
        CardList cardList = DataUtils.getCardListPersist();
        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.of(cardList));
        BDDMockito.given(cardListMapper.map(any(CardList.class))).willReturn(new CardListDto());

        // when
        CardListDto cardListDto = cardListService.findCardListById(1L, 1L);

        // then
        assertNotNull(cardListDto);
        verify(cardListRepository, times(1)).findByBoardIdAndId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Test delete method throws exception if card list not found")
    public void givenBoardIdAndCardListId_whenDelete_thenThrowException() {
        // given
        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardListService.delete(1L, 1L));
    }

    @Test
    @DisplayName("Test delete method deletes card list")
    public void givenBoardIdAndCardListId_whenDelete_thenDeleteCardList() {
        // given
        CardList cardList = DataUtils.getCardListPersist();
        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.of(cardList));

        // when
        cardListService.delete(1L, 1L);

        // then
        verify(cardListRepository, times(1)).delete(any(CardList.class));
    }

    @Test
    @DisplayName("Test update method throws exception if card list not found")
    public void givenCardListUpdateDto_whenUpdate_thenThrowException() {
        // given
        CardListUpdateDto dto = new CardListUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));

        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> cardListService.update(dto, 1L, 1L));
    }

    @Test
    @DisplayName("Test update method updates card list")
    public void givenCardListUpdateDto_whenUpdate_thenUpdateCardList() {
        // given
        CardListUpdateDto dto = new CardListUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));

        CardList cardList = DataUtils.getCardListPersist();
        BDDMockito.given(cardListRepository.findByBoardIdAndId(anyLong(), anyLong())).willReturn(Optional.of(cardList));
        BDDMockito.doNothing().when(cardListMapper).update(any(CardListUpdateDto.class), any(CardList.class));
        BDDMockito.given(cardListRepository.save(any(CardList.class))).willReturn(cardList);
        BDDMockito.given(cardListMapper.map(any(CardList.class))).willReturn(new CardListDto());

        // when
        CardListDto updatedCardList = cardListService.update(dto, 1L, 1L);

        // then
        assertNotNull(updatedCardList);
        verify(cardListRepository, times(1)).save(any(CardList.class));
    }
}

