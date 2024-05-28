package com.suhoi.demo.service;

import com.suhoi.demo.dto.CardListCreateDto;
import com.suhoi.demo.dto.CardListDto;
import com.suhoi.demo.dto.CardListUpdateDto;
import com.suhoi.demo.model.CardList;

import java.util.List;

public interface CardListService {

    /**
     * Создание листа карт с проверкой на принадлежность аутентифицированного пользователя к доске,
     * в которой создан лист карт
     *
     * @param dto
     * @param boardId
     * @return
     */
    CardList createCardList(CardListCreateDto dto, Long boardId);

    /**
     * Поиск всех листов карт с проверкой на принадлежность аутентифицированного пользователя к доске,
     * в которой создан лист карт
     *
     * @param boardId
     * @return
     */
    List<CardListDto> findCardListByBoardId(Long boardId);

    /**
     * Поиск листа карт по id с проверкой на принадлежность аутентифицированного пользователя к доске,
     * в которой создан лист карт
     *
     * @param boardId
     * @param id
     * @return
     */
    CardListDto findCardListById(Long boardId, Long id);

    /**
     * Удаление листа карт с проверкой на принадлежность аутентифицированного пользователя к доске,
     * в которой создан лист карт
     *
     * @param boardId
     * @param id
     */
    void delete(Long boardId, Long id);

    /**
     * Обновление листа карт с проверкой на принадлежность аутентифицированного пользователя к доске,
     * в которой создан лист карт
     *
     * @param dto
     * @param boardId
     * @param id
     * @return
     */
    CardListDto update(CardListUpdateDto dto, Long boardId, Long id);
}
