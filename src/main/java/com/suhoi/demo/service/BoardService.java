package com.suhoi.demo.service;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;

import java.util.List;

public interface BoardService {

    Board create(BoardCreateDto dto);

    BoardDto update(BoardUpdateDto dto, Long boardId);

    void delete(Long boardId);

    List<BoardDto> getAll();

    BoardDto findById(Long boardId);
}
