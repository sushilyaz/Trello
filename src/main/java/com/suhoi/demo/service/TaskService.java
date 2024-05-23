package com.suhoi.demo.service;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.model.Task;

import java.util.List;

public interface TaskService {

    Task create(TaskCreateDto dto, Long boardId, Long cardId);

    TaskDto findById(Long id, Long boardId);

    List<TaskDto> findAllByCard(Long cardId, Long boardId);

    TaskDto update(Long id, Long cardId);

    void delete(Long id, Long boardId);
}
