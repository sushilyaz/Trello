package com.suhoi.demo.in.controller;

import com.suhoi.demo.dto.CardCreateDto;
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

    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardCreateDto dto,
                           @PathVariable Long boardId,
                           @PathVariable Long cardListsId,
                           BindingResult bindingResult,
                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            Card card = cardService.createCard(dto, cardListsId);
            return ResponseEntity
                    .created(uriBuilder.path("/api/boards/" + boardId + "/card-lists/" + cardListsId + "/cards/{cardId}")
                            .buildAndExpand(card.getId()).toUri())
                    .body(card);
        }
    }

    @GetMapping
    public ResponseEntity<List<Card>> getCards(@PathVariable Long cardListsId) {
        List<Card> allCards = cardService.findAllCards(cardListsId);
        return allCards.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allCards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCard(@PathVariable Long cardId, @PathVariable Long cardListsId) {
        Card cardById = cardService.findCardById(cardId, cardListsId);
        return ResponseEntity.ok(cardById);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId, @PathVariable Long cardListsId) {
        cardService.deleteCardById(cardId, cardListsId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @RequestBody CardUpdateDto dto) {
        cardService.update(dto, cardId, cardListsId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/burned-cards")
    public ResponseEntity<List<Card>> getDeadlineBurnedCards(@PathVariable Long cardListsId) {
        List<Card> burnedCards = cardService.findBurnedCards(cardListsId);
        return ResponseEntity.ok(burnedCards);
    }
}
