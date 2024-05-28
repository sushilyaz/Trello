package com.suhoi.demo.service.impl;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.BoardMapper;
import com.suhoi.demo.model.AccessType;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.BoardService;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final BoardMapper boardMapper;

    @Override
    public Board create(BoardCreateDto dto) {
        Board board = boardMapper.map(dto);
        boardRepository.save(board);
        return board;
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public BoardDto update(BoardUpdateDto dto, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("Board with id " + boardId + " not found"));
        boardMapper.update(dto, board);
        boardRepository.save(board);
        return boardMapper.map(board);
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public void delete(Long boardId) {
        List<Board> boardsByCreatorId = boardRepository.findBoardsByCreatorId(userUtils.getCurrentUser().getId());
        Board candidateForDeletion = null;
        for (Board board : boardsByCreatorId) {
            if (board.getCreator().getId().equals(userUtils.getCurrentUser().getId()) && board.getModerators().contains(userUtils.getCurrentUser())) {
                candidateForDeletion = board;
            }
        }
        if (candidateForDeletion != null) {
            boardRepository.delete(candidateForDeletion);
        } else {
            throw new DataNotFoundException("Data not found");
        }
    }

    @Override
    public List<BoardDto> getAll() {
        List<Board> boards = boardRepository.findBoardsByMembersId(userUtils.getCurrentUser().getId());
        return boards.stream()
                .map(boardMapper::map)
                .toList();
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public BoardDto findById(Long boardId) {
        Board board = boardRepository.findByIdAndUserId(userUtils.getCurrentUser().getId(), boardId)
                .orElseThrow(() -> new DataNotFoundException("Board with id " + boardId + " not found"));
        return boardMapper.map(board);
    }
}
