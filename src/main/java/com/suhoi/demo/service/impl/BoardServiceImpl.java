package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
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
        Board board = boardMapper.map(dto, members, moderators);
        board.setCreator(currentUser);
        boardRepository.save(board);
        return board;
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
