package com.suhoi.demo.service;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;

import java.util.List;

public interface BoardService {

    Board create(BoardCreateDto dto);

    void update(BoardUpdateDto dto);

    void delete(Long id);

    List<BoardDto> getAll();
}
