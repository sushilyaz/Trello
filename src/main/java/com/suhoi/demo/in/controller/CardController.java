package com.suhoi.demo.in.controller;

import com.suhoi.demo.annotation.Loggable;
import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/card-lists/{cardListsId}/cards")
public class CardController {

    private final CardService cardService;

    @Loggable
    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardCreateDto dto,
                           @PathVariable Long boardId,
                           @PathVariable Long cardListsId,
                           BindingResult bindingResult,
                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            Card card = cardService.createCard(dto, cardListsId, boardId);
            return ResponseEntity
                    .created(uriBuilder.path("/api/boards/" + boardId + "/card-lists/" + cardListsId + "/cards/{cardId}")
                            .buildAndExpand(card.getId()).toUri())
                    .body(card);
        }
    }

    @Loggable
    @GetMapping
    public ResponseEntity<List<CardDto>> getCards(@PathVariable Long cardListsId,
                                                  @PathVariable Long boardId) {
        List<CardDto> allCards = cardService.findAllCards(cardListsId, boardId);
        return allCards.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allCards);
    }

    @Loggable
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId) {
        CardDto cardById = cardService.findCardById(cardId, cardListsId, boardId);
        return ResponseEntity.ok(cardById);
    }

    @Loggable
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId) {
        cardService.deleteCardById(cardId, cardListsId, boardId);
        return ResponseEntity.noContent().build();
    }

    @Loggable
    @PatchMapping("/{cardId}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId,
                                           @RequestBody CardUpdateDto dto) {
        CardDto update = cardService.update(dto, cardId, cardListsId, boardId);
        return ResponseEntity.ok(update);
    }

    @Loggable
    @GetMapping("/burned-cards")
    public ResponseEntity<List<CardDto>> getDeadlineBurnedCards(@PathVariable Long cardListsId,
                                                                @PathVariable Long boardId) {
        List<CardDto> burnedCards = cardService.findBurnedCards(cardListsId, boardId);
        return ResponseEntity.ok(burnedCards);
    }
}
