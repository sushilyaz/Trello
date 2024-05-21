package com.suhoi.demo.in.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6ProxySerializer;
import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.service.CardListService;
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
@RequestMapping("/api/boards/{boardId}/card-lists")
public class CardListController {

    private final CardListService cardListService;

    @PostMapping
    public ResponseEntity<CardList> createCardList(@Valid @RequestBody CardListCreateDto dto,
                                             @PathVariable Long boardId,
                                             BindingResult bindingResult,
                                             UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            CardList cardList = cardListService.createCardList(dto, boardId);
            return ResponseEntity
                    .created(uriBuilder.path("/api/boards/" + boardId + "/card-lists/{listId}")
                            .buildAndExpand(cardList.getId()).toUri())
                    .body(cardList);
        }
    }

    @GetMapping
    public ResponseEntity<List<CardList>> getCardLists(@PathVariable Long boardId) {
        List<CardList> cardListByBoardId = cardListService.findCardListByBoardId(boardId);
        return ResponseEntity.ok(cardListByBoardId);
    }

    @GetMapping("/{cardListId}")
    public ResponseEntity<CardList> getCardList(@PathVariable Long boardId, @PathVariable Long cardListId) {
        CardList cardListById = cardListService.findCardListById(boardId, cardListId);
        return ResponseEntity.ok(cardListById);
    }

    @DeleteMapping("/{cardListId}")
    public ResponseEntity<Void> deleteCardList(@PathVariable Long boardId, @PathVariable Long cardListId) {
        cardListService.delete(boardId, cardListId);
        return ResponseEntity.noContent().build();
    }
}
