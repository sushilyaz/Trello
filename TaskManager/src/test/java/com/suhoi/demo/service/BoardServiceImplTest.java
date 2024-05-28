package com.suhoi.demo.service;

import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.BoardMapper;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.service.impl.BoardServiceImpl;
import com.suhoi.demo.util.DataUtils;
import com.suhoi.demo.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private BoardMapper boardMapper;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    @DisplayName("Test create method saves board")
    public void givenBoardCreateDto_whenCreate_thenSaveBoard() {
        // given
        BoardCreateDto dto = new BoardCreateDto();
        dto.setTitle("Sample Board");
        dto.setDescription("Sample Description");
        dto.setModerators(List.of("moderator1"));
        dto.setMembers(List.of("member1"));

        Board board = DataUtils.getBoardPersist();
        BDDMockito.given(boardMapper.map(any(BoardCreateDto.class))).willReturn(board);
        BDDMockito.given(boardRepository.save(any(Board.class))).willReturn(board);

        // when
        Board createdBoard = boardService.create(dto);

        // then
        assertEquals(board, createdBoard);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("Test update method throws exception if board not found")
    public void givenBoardUpdateDto_whenUpdate_thenThrowException() {
        // given
        BoardUpdateDto dto = new BoardUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));

        BDDMockito.given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> boardService.update(dto, 1L));
    }

    @Test
    @DisplayName("Test update method updates board")
    public void givenBoardUpdateDto_whenUpdate_thenUpdateBoard() {
        // given
        BoardUpdateDto dto = new BoardUpdateDto();
        dto.setTitle(JsonNullable.of("Updated Title"));
        dto.setDescription(JsonNullable.of("Updated Description"));

        Board board = DataUtils.getBoardPersist();
        BDDMockito.given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        BDDMockito.doNothing().when(boardMapper).update(any(BoardUpdateDto.class), any(Board.class));
        BDDMockito.given(boardRepository.save(any(Board.class))).willReturn(board);
        BDDMockito.given(boardMapper.map(any(Board.class))).willReturn(new BoardDto());

        // when
        BoardDto updatedBoard = boardService.update(dto, 1L);

        // then
        assertNotNull(updatedBoard);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("Test delete method throws exception if board not found")
    public void givenBoardId_whenDelete_thenThrowException() {
        // given
        BDDMockito.given(boardRepository.findBoardsByCreatorId(anyLong())).willReturn(List.of());
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(DataUtils.getJohnPersist());

        // when / then
        assertThrows(DataNotFoundException.class, () -> boardService.delete(1L));
    }

    @Test
    @DisplayName("Test getAll method returns list of BoardDto")
    public void whenGetAll_thenReturnListOfBoardDto() {
        // given
        Board board = DataUtils.getBoardPersist();
        BDDMockito.given(boardRepository.findBoardsByMembersId(anyLong())).willReturn(List.of(board));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(DataUtils.getJohnPersist());
        BDDMockito.given(boardMapper.map(any(Board.class))).willReturn(new BoardDto());

        // when
        List<BoardDto> boardDtos = boardService.getAll();

        // then
        assertFalse(boardDtos.isEmpty());
        verify(boardRepository, times(1)).findBoardsByMembersId(anyLong());
    }

    @Test
    @DisplayName("Test findById method throws exception if board not found")
    public void givenBoardId_whenFindById_thenThrowException() {
        // given
        BDDMockito.given(boardRepository.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(DataUtils.getJohnPersist());

        // when / then
        assertThrows(DataNotFoundException.class, () -> boardService.findById(1L));
    }

    @Test
    @DisplayName("Test findById method returns BoardDto")
    public void givenBoardId_whenFindById_thenReturnBoardDto() {
        // given
        Board board = DataUtils.getBoardPersist();
        BDDMockito.given(boardRepository.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(board));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(DataUtils.getJohnPersist());
        BDDMockito.given(boardMapper.map(any(Board.class))).willReturn(new BoardDto());

        // when
        BoardDto boardDto = boardService.findById(1L);

        // then
        assertNotNull(boardDto);
        verify(boardRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
    }
}

