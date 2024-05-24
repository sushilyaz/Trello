package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.model.Card;

import java.util.List;

public interface CardService {

    Card createCard(CardCreateDto dto, Long cardListId, Long boardId);

    CardDto findCardById(Long cardId, Long cardListId, Long boardId);

    List<CardDto> findAllCards(Long cardListId, Long boardId);

    CardDto update (CardUpdateDto dto, Long cardId, Long cardListId, Long boardId);

    void deleteCardById(Long cardId, Long cardListId, Long boardId);

    List<CardDto> findBurnedCards(Long cardListId, Long boardId);
}
