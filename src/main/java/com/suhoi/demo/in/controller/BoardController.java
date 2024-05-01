package com.suhoi.demo.in.controller;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody BoardCreateDto dto,
                                             BindingResult bindingResult,
                                             UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            Board board = boardService.create(dto);
            return ResponseEntity
                    .created(uriBuilder.path("/api/boards/{id}")
                            .buildAndExpand(board.getId()).toUri())
                    .body(board);
        }
    }
}
