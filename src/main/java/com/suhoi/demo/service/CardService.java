package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.model.Card;

import java.util.List;

public interface CardService {

    Card createCard(CardCreateDto dto, Long cardListId);

    Card findCardById(Long cardId, Long cardListId);

    List<Card> findAllCards(Long cardListId);

    void update (CardUpdateDto dto, Long cardId, Long cardListId);

    void deleteCardById(Long cardId, Long cardListId);

    List<Card> findBurnedCards(Long cardListId);
}
