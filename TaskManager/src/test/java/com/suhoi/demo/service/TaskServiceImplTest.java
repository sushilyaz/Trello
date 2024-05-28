package com.suhoi.demo.service;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.TaskMapper;
import com.suhoi.demo.model.*;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.repository.TaskRepository;
import com.suhoi.demo.service.impl.TaskServiceImpl;
import com.suhoi.demo.util.DataUtils;
import com.suhoi.demo.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Test create method saves task")
    public void givenTaskCreateDto_whenCreate_thenSaveTask() {
        // given
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Sample Task");
        dto.setDescription("Sample Description");
        Card card = DataUtils.getCardPersist();
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        BDDMockito.given(cardRepository.findById(anyLong())).willReturn(Optional.of(card));
        BDDMockito.given(taskMapper.map(any(TaskCreateDto.class))).willReturn(task);
        BDDMockito.given(taskRepository.save(any(Task.class))).willReturn(task);

        // when
        Task createdTask = taskService.create(dto, 1L, card.getId(), card.getCardList().getId());

        // then
        assertEquals(task, createdTask);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test create method throws exception if card not found")
    public void givenTaskCreateDto_whenCreate_thenThrowException() {
        // given
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Sample Task");
        dto.setDescription("Sample Description");

        BDDMockito.given(cardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> taskService.create(dto, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test findById method returns TaskDto")
    public void givenTaskId_whenFindById_thenReturnTaskDto() {
        // given
        Task task = DataUtils.getTaskPersist();
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        BDDMockito.given(taskMapper.map(any(Task.class))).willReturn(new TaskDto());

        // when
        TaskDto taskDto = taskService.findById(1L, 1L, 1L);

        // then
        assertNotNull(taskDto);
        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test findById method throws exception if task not found")
    public void givenTaskId_whenFindById_thenThrowException() {
        // given
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> taskService.findById(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test findAllByCard method returns list of TaskDto")
    public void givenCardId_whenFindAllByCard_thenReturnListOfTaskDto() {
        // given
        Task task = DataUtils.getTaskPersist();
        BDDMockito.given(taskRepository.findTasksByCardId(anyLong())).willReturn(List.of(task));
        BDDMockito.given(taskMapper.map(any(Task.class))).willReturn(new TaskDto());

        // when
        List<TaskDto> taskDtos = taskService.findAllByCard(1L, 1L, 1L);

        // then
        assertFalse(taskDtos.isEmpty());
        verify(taskRepository, times(1)).findTasksByCardId(anyLong());
    }

    @Test
    @DisplayName("Test update method updates task")
    public void givenTaskId_whenUpdate_thenUpdateTask() {
        // given
        Task task = DataUtils.getTaskPersist();
        Card card = task.getCard();
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(card.getAssignees().get(0));
        BDDMockito.given(taskRepository.save(any(Task.class))).willReturn(task);
        BDDMockito.given(taskMapper.map(any(Task.class))).willReturn(new TaskDto());

        // when
        TaskDto updatedTask = taskService.update(1L, card.getId(), card.getCardList().getBoard().getId(), card.getCardList().getId());

        // then
        assertNotNull(updatedTask);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test update method throws exception if task not found")
    public void givenTaskId_whenUpdate_thenThrowException() {
        // given
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> taskService.update(1L, 1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test update method throws exception if user is not assignee")
    public void givenTaskId_whenUpdate_thenThrowAccessDeniedException() {
        // given
        Task task = DataUtils.getTaskPersist();
        Card card = task.getCard();
        User user = DataUtils.getJohnPersist();
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        BDDMockito.given(userUtils.getCurrentUser()).willReturn(user);

        // when / then
        assertThrows(AccessPermissionDeniedException.class, () -> taskService.update(1L, card.getId(), card.getCardList().getBoard().getId(), card.getCardList().getId()));
    }

    @Test
    @DisplayName("Test delete method deletes task")
    public void givenTaskId_whenDelete_thenDeleteTask() {
        // given
        Task task = DataUtils.getTaskPersist();
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        // when
        taskService.delete(1L, 1L, 1L);

        // then
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    @DisplayName("Test delete method throws exception if task not found")
    public void givenTaskId_whenDelete_thenThrowException() {
        // given
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(DataNotFoundException.class, () -> taskService.delete(1L, 1L, 1L));
    }
}

