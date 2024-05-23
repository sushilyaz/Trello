package com.suhoi.demo.service.impl;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.mapper.TaskMapper;
import com.suhoi.demo.model.*;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.repository.TaskRepository;
import com.suhoi.demo.service.TaskService;
import com.suhoi.demo.util.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CardRepository cardRepository;

    private final UserUtils userUtils;

    private final TaskMapper taskMapper;

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public Task create(TaskCreateDto dto, Long boardId, Long cardId) {
        Task task = taskMapper.map(dto);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("Card not found"));
        task.setCard(card);
        return taskRepository.save(task);
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public TaskDto findById(Long id, Long boardId) {
        Task task = findById(id);
        return taskMapper.map(task);
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Override
    public List<TaskDto> findAllByCard(Long cardId, Long boardId) {
        return taskRepository.findTasksByCardId(cardId).stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    @CheckAccessByBoard(value = AccessType.MEMBER)
    @Transactional
    @Override
    public TaskDto update(Long id, Long cardId) {
        Task task = findById(id);
        Card card = task.getCard();
        List<User> assignees = card.getAssignees();
        if (!assignees.contains(userUtils.getCurrentUser())) {
            throw new AccessPermissionDeniedException("You are not assignee of this card");
        }
        task.setComplete(true);
        if (card.getStatus().equals(Status.NEW)) {
            card.setStatus(Status.DOING);
        }
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @CheckAccessByBoard(value = AccessType.MODERATOR)
    @Override
    public void delete(Long id, Long boardId) {
        Task task = findById(id);
        taskRepository.delete(task);
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Task not found"));
    }
}
