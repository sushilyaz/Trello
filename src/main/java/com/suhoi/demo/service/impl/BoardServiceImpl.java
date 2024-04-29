package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.service.BoardService;

import java.util.List;

public class BoardServiceImpl implements BoardService {
    @Override
    public Board create(BoardCreateDto dto) {
        return null;
    }

    @Override
    public void update(BoardUpdateDto dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<BoardDto> getAll() {
        return List.of();
    }
}
