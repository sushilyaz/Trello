package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardMapper;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.service.CardService;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;
    private final CardMapper cardMapper;
    private final UserUtils userUtils;

    @Override
    public Card createCard(CardCreateDto dto, Long cardListId) {
        CardList cardList = cardListRepository.findById(cardListId).orElseThrow(() -> new DataNotFoundException("CardList not found"));
        Card card = cardMapper.map(dto);
        card.setCardList(cardList);
        return cardRepository.save(card);
    }

    @Override
    public Card findCardById(Long cardId, Long cardListId) {
        return cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found"));
    }

    @Override
    public List<Card> findAllCards(Long cardListId) {
        return cardRepository.findCardsByCardListId(cardListId);
    }

    @Override
    public void update(CardUpdateDto dto, Long cardId, Long cardListId) {
        Card card = cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found"));
        cardMapper.update(card, dto);
        cardRepository.save(card);
    }

    @Override
    public void deleteCardById(Long cardId, Long cardListId) {
        Card card = cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Card not found"));
        if (card.getCreator().equals(userUtils.getCurrentUser())) {
            cardRepository.delete(card);
        } else {
            throw new AccessPermissionDeniedException("You do not have permission to delete this card ");
        }
    }

    @Override
    public List<Card> findBurnedCards(Long cardListId) {
        return cardRepository.findCardsByCardListIdAndBurned(cardListId, true);
    }
}
