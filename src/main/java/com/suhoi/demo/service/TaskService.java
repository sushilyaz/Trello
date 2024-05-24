package com.suhoi.demo.service;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.model.Task;

import java.util.List;

public interface TaskService {

    Task create(TaskCreateDto dto, Long boardId, Long cardId, Long cardListId);

    TaskDto findById(Long id, Long boardId, Long cardListId);

    List<TaskDto> findAllByCard(Long cardId, Long boardId, Long cardListId);

    TaskDto update(Long id, Long cardId, Long boardId, Long cardListId);

    void delete(Long id, Long boardId, Long cardListId);
}
