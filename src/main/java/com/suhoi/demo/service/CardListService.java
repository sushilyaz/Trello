package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.model.CardList;

import java.util.List;

public interface CardListService {

    CardList createCardList(CardListCreateDto dto, Long boardId);

    List<CardListDto> findCardListByBoardId(Long boardId);

    CardListDto findCardListById(Long boardId, Long id);

    void delete(Long boardId, Long id);

    CardListDto update(CardListUpdateDto dto, Long boardId, Long id);
}
