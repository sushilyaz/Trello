package com.suhoi.demo.service;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;

import java.util.List;

public interface BoardService {

    /**
     * Создание новой доски для аутентифицированного пользователя
     *
     * @param dto
     * @return
     */
    Board create(BoardCreateDto dto);

    /**
     * Полное и частичное обновление доски
     *
     * @param dto
     * @param boardId
     * @return
     */
    BoardDto update(BoardUpdateDto dto, Long boardId);

    /**
     * Удаление доски по id
     *
     * @param boardId
     */
    void delete(Long boardId);

    /**
     * Получение всех досок
     *
     * @return
     */
    List<BoardDto> getAll();

    /**
     * Поиск доски по id
     *
     * @param boardId
     * @return
     */
    BoardDto findById(Long boardId);
}
