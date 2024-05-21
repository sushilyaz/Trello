package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.BoardMapper;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.BoardService;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final BoardMapper boardMapper;

    @Override
    public Board create(BoardCreateDto dto) {
        List<User> members = userRepository.findByEmailIn(dto.getMembers());
        List<User> moderators = userRepository.findByEmailIn(dto.getModerators());
        User currentUser = userUtils.getCurrentUser();
        members.add(currentUser);
        moderators.add(currentUser);
        Board board = boardMapper.map(dto, moderators, members);
        board.setCreator(currentUser);
        boardRepository.save(board);
        return board;
    }

    @Override
    public void update(BoardUpdateDto dto, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Board with id " + id + " not found"));
        boardMapper.update(dto, board);
        if (board.getCreator().getId().equals(userUtils.getCurrentUser().getId()) && board.getModerators().contains(userUtils.getCurrentUser())) {
            boardRepository.save(board);
        } else {
            throw new AccessPermissionDeniedException("You cant update with board");
        }
    }

    @Override
    public void delete(Long id) {
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
    public List<Board> getAll() {
//        List<Board> boards = userUtils.getCurrentUser().getBoards();
        return boardRepository.findBoardsByMembersId(userUtils.getCurrentUser().getId());
    }

    @Override
    public Board findById(Long id) {
        Board board = boardRepository.findByIdAndUserId(userUtils.getCurrentUser().getId(), id)
                .orElseThrow(() -> new DataNotFoundException("Board with id " + id + " not found"));
        if (board.getCreator().getId().equals(userUtils.getCurrentUser().getId()) && board.getModerators().contains(userUtils.getCurrentUser())) {
            return board;
        } else {
            throw new AccessPermissionDeniedException("You cant open with board");
        }
    }
}
