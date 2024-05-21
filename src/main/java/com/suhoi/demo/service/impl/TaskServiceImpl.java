package com.suhoi.demo.service.impl;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskUpdateDto;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public Task create(TaskCreateDto dto) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long id, Long cardId) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByCard(Long cardId) {
        return List.of();
    }

    @Override
    public void update(TaskUpdateDto dto, Long id, Long cardId) {

    }
}
