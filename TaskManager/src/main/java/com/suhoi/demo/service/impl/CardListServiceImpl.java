package com.suhoi.demo.service.impl;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardListMapper;
import com.suhoi.demo.model.AccessType;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.service.CardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardListServiceImpl implements CardListService {

    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;
    private final CardListMapper cardListMapper;

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public CardList createCardList(CardListCreateDto dto, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("Board not found"));
        CardList cardList = cardListMapper.map(dto);
        cardList.setBoard(board);
        return cardListRepository.save(cardList);
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public List<CardListDto> findCardListByBoardId(Long boardId) {
        List<CardList> cardLists = cardListRepository.findByBoardId(boardId);

        return cardLists.stream()
                .map(cardListMapper::map)
                .toList();
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)

    @Override
    public CardListDto findCardListById(Long boardId, Long id) {
        CardList cardList = cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));

        return cardListMapper.map(cardList);
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public void delete(Long boardId, Long id) {
        CardList cardList = cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));

        cardListRepository.delete(cardList);
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public CardListDto update(CardListUpdateDto dto, Long boardId, Long id) {
        CardList cardList = cardListRepository.findByBoardIdAndId(boardId, id)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));
        cardListMapper.update(dto, cardList);
        cardListRepository.save(cardList);
        return cardListMapper.map(cardList);
    }
}
