package com.suhoi.demo.in.controller;

import com.suhoi.demo.annotation.Loggable;
import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.service.BoardService;
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
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Loggable
    @PostMapping()
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

    @Loggable
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long id) {
        BoardDto board = boardService.findById(id);
        return ResponseEntity.ok(board);
    }
    @Loggable
    @GetMapping
    public ResponseEntity<List<BoardDto>> getBoards() {
        List<BoardDto> boardDtos = boardService.getAll();
        return ResponseEntity
                .ok(boardDtos);
    }

    @Loggable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@RequestParam Long id) {
        boardService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Loggable
    @PatchMapping("/{id}")
    public ResponseEntity<BoardDto> updateBoard(@Valid @RequestBody BoardUpdateDto dto, @PathVariable Long id) {
        BoardDto boardDto = boardService.update(dto, id);
        return ResponseEntity.ok(boardDto);
    }
}
