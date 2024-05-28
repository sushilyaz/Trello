package com.suhoi.demo.in.controller;

import com.suhoi.demo.annotation.Auditable;
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

    /**
     * Апи создание карты. с валидацией вводимых данных
     *
     * @param dto
     * @param boardId
     * @param cardListsId
     * @param bindingResult
     * @param uriBuilder
     * @return
     * @throws BindException
     */
    @Auditable
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

    /**
     * Апи для получения всех карты аутентифицированного пользователя
     *
     * @param cardListsId
     * @param boardId
     * @return
     */
    @Auditable
    @Loggable
    @GetMapping
    public ResponseEntity<List<CardDto>> getCards(@PathVariable Long cardListsId,
                                                  @PathVariable Long boardId) {
        List<CardDto> allCards = cardService.findAllCards(cardListsId, boardId);
        return allCards.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allCards);
    }

    /**
     * Апи для получение карты по ее id
     *
     * @param cardId
     * @param cardListsId
     * @param boardId
     * @return
     */
    @Auditable
    @Loggable
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId) {
        CardDto cardById = cardService.findCardById(cardId, cardListsId, boardId);
        return ResponseEntity.ok(cardById);
    }

    /**
     * Апи удаление карты по ее id
     *
     * @param cardId
     * @param cardListsId
     * @param boardId
     * @return
     */
    @Auditable
    @Loggable
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId) {
        cardService.deleteCardById(cardId, cardListsId, boardId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Апи для полного или частичного обновления карты
     *
     * @param cardId
     * @param cardListsId
     * @param boardId
     * @param dto
     * @return
     */
    @Auditable
    @Loggable
    @PatchMapping("/{cardId}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long cardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable Long boardId,
                                           @RequestBody CardUpdateDto dto) {
        CardDto update = cardService.update(dto, cardId, cardListsId, boardId);
        return ResponseEntity.ok(update);
    }

    @Auditable
    @Loggable
    @GetMapping("/burned-cards")
    public ResponseEntity<List<CardDto>> getDeadlineBurnedCards(@PathVariable Long cardListsId,
                                                                @PathVariable Long boardId) {
        List<CardDto> burnedCards = cardService.findBurnedCards(cardListsId, boardId);
        return ResponseEntity.ok(burnedCards);
    }
}
