package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.model.CardList;

import java.util.List;

public interface CardListService {

    CardList createCardList(CardListCreateDto dto, Long boardId);

    List<CardList> findCardListByBoardId(Long boardId);

    CardList findCardListById(Long boardId, Long id);

    void delete(Long boardId, Long id);

    void update(CardListUpdateDto dto, Long boardId, Long id);
}
