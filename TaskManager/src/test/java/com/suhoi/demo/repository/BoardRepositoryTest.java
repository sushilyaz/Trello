package com.suhoi.demo.repository;

import com.suhoi.demo.container.PostgresContainer;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardRepositoryTest extends PostgresContainer {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        boardRepository.deleteAll();
        userRepository.deleteAll();
        System.out.println("delete all");
    }

    @Test
    @DisplayName("Test save board functionality")
    public void givenBoard_whenSave_thenBoardIsSaved() {
        //given
        Board boardToSave = DataUtils.getAnotherBoardTransient();
        //when
        Board savedBoard = boardRepository.save(boardToSave);
        //then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test findAll boards functionality")
    public void givenBoards_whenSave_thenBoardsReturned() {
        //given
        Board board1 = DataUtils.getBoardTransient();
        Board board2 = DataUtils.getAnotherBoardTransient();

        boardRepository.saveAll(List.of(board1, board2));
        //when
        Iterable<Board> boards = boardRepository.findAll();
        //then
        assertThat(boards).isNotNull();
        assertThat(((Collection<?>) boards).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test findById board functionality. Board is found")
    public void givenBoard_whenFindById_thenBoardIsFound() {
        //given
        Board boardToSave = DataUtils.getBoardTransient();

        boardRepository.save(boardToSave);
        //when
        Board board = boardRepository.findById(boardToSave.getId()).orElse(null);
        //then
        assertThat(board).isNotNull();
    }

    @Test
    @DisplayName("Test findById board functionality. Board is not found")
    public void givenBoard_whenFindById_thenBoardIsNotFound() {
        //given
        Board boardToSave = DataUtils.getBoardPersist();
        //when
        Board board = boardRepository.findById(boardToSave.getId()).orElse(null);
        //then
        assertThat(board).isNull();
    }

    @Test
    @DisplayName("Test findBoardsByMembersId functionality")
    public void givenBoardsSaved_whenFindBoardsByMembersId_thenBoardsAreFound() {
        //given
        Board board1 = DataUtils.getBoardTransient();
        Board board2 = DataUtils.getAnotherBoardTransient();

        boardRepository.saveAll(List.of(board1, board2));
        //when
        User user = DataUtils.getJohnTransient();
        user = userRepository.save(user);

        board1.setMembers(Collections.singleton(user));
        board2.setMembers(Collections.singleton(user));

        boardRepository.saveAll(List.of(board1, board2));

        //when
        List<Board> boards = boardRepository.findBoardsByMembersId(user.getId());

        //then
        assertThat(boards).isNotNull();
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test findByIdAndUserId functionality. Board is not found")
    public void givenBoardNotSaved_whenFindByIdAndUserId_thenBoardIsNotFound() {
        //given
        User user = userRepository.save(DataUtils.getJohnTransient());
        User managedUser = userRepository.findById(user.getId()).orElseThrow();

        Board board = DataUtils.getBoardPersist();
        //when
        Optional<Board> foundBoard = boardRepository.findByIdAndUserId(managedUser.getId(), board.getId());
        //then
        assertThat(foundBoard).isNotPresent();
    }
}

