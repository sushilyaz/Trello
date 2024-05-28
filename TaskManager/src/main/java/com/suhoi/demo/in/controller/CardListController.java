package com.suhoi.demo.in.controller;

import com.suhoi.demo.annotation.Auditable;
import com.suhoi.demo.annotation.Loggable;
import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListDto;
import com.suhoi.demo.dto.CardListUpdateDto;
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

    /**
     * Апи создание листа карт
     *
     * @param dto
     * @param boardId
     * @param bindingResult
     * @param uriBuilder
     * @return
     * @throws BindException
     */
    @Auditable
    @Loggable
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

    /**
     * Апи получение всех листов аутентицифированного пользователя
     *
     * @param boardId
     * @return
     */
    @Auditable
    @Loggable
    @GetMapping
    public ResponseEntity<List<CardListDto>> getCardLists(@PathVariable Long boardId) {
        List<CardListDto> cardListByBoardId = cardListService.findCardListByBoardId(boardId);
        return ResponseEntity.ok(cardListByBoardId);
    }

    /**
     * Апи получения листа по id аутентифицированного пользователя
     *
     * @param boardId
     * @param cardListId
     * @return
     */
    @Auditable
    @Loggable
    @GetMapping("/{cardListId}")
    public ResponseEntity<CardListDto> getCardList(@PathVariable Long boardId, @PathVariable Long cardListId) {
        CardListDto cardListById = cardListService.findCardListById(boardId, cardListId);
        return ResponseEntity.ok(cardListById);
    }

    /**
     * Апи для обновления листа карт
     *
     * @param boardId
     * @param cardListId
     * @param dto
     * @return
     */
    @Auditable
    @Loggable
    @PatchMapping("/{cardListId}")
    public ResponseEntity<CardListDto> updateCardList(@PathVariable Long boardId,
                                                      @PathVariable Long cardListId,
                                                      @RequestBody CardListUpdateDto dto) {
        CardListDto update = cardListService.update(dto, boardId, cardListId);
        return ResponseEntity.ok(update);
    }

    /**
     * Удаление листа карт
     *
     * @param boardId
     * @param cardListId
     * @return
     */
    @Auditable
    @Loggable
    @DeleteMapping("/{cardListId}")
    public ResponseEntity<Void> deleteCardList(@PathVariable Long boardId, @PathVariable Long cardListId) {
        cardListService.delete(boardId, cardListId);
        return ResponseEntity.noContent().build();
    }
}
