package com.suhoi.demo.service;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskUpdateDto;
import com.suhoi.demo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task create(TaskCreateDto dto);

    Optional<Task> findById(Long id, Long cardId);

    List<Task> findAllByCard(Long cardId);

    void update(TaskUpdateDto dto, Long id, Long cardId);
}
