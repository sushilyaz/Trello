package com.suhoi.demo.service.impl;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.CardMapper;
import com.suhoi.demo.model.AccessType;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.service.CardService;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;
    private final CardMapper cardMapper;
    private final UserUtils userUtils;

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public Card createCard(CardCreateDto dto, Long cardListId, Long boardId) {
        CardList cardList = cardListRepository.findById(cardListId).orElseThrow(() -> new DataNotFoundException("CardList not found"));
        Card card = cardMapper.map(dto);
        card.setCardList(cardList);
        return cardRepository.save(card);
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public CardDto findCardById(Long cardId, Long cardListId, Long boardId) {
        Card card = cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found"));
        card.getCardList().getBoard().getMembers().contains(userUtils.getCurrentUser());
        return cardMapper.map(card);
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public List<CardDto> findAllCards(Long cardListId, Long boardId) {
        List<Card> cards = cardRepository.findCardsByCardListId(cardListId);
        return cards.stream()
                .map(cardMapper::map)
                .toList();
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public CardDto update(CardUpdateDto dto, Long cardId, Long cardListId, Long boardId) {
        Card card = cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found"));
        cardMapper.update(card, dto);
        cardRepository.save(card);
        return cardMapper.map(card);
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public void deleteCardById(Long cardId, Long cardListId, Long boardId) {
        Card card = cardRepository.findCardByIdAndCardListId(cardId, cardListId)
                .orElseThrow(() -> new DataNotFoundException("Card not found"));
        if (card.getCreator().equals(userUtils.getCurrentUser())) {
            cardRepository.delete(card);
        } else {
            throw new AccessPermissionDeniedException("You do not have permission to delete this card ");
        }
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public List<CardDto> findBurnedCards(Long cardListId, Long boardId) {
        List<Card> cards = cardRepository.findCardsByCardListIdAndBurned(cardListId, true);
        return cards.stream()
                .map(cardMapper::map)
                .toList();
    }
}
