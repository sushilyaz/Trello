package com.suhoi.demo.in.controller;

import com.suhoi.demo.annotation.Loggable;
import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/card-lists/{cardListsId}/cards/{cardId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @Loggable
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreateDto dto,
                                           @PathVariable("boardId") Long boardId,
                                           @PathVariable Long cardListsId,
                                           @PathVariable("cardId") Long cardId,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            Task task = taskService.create(dto, boardId, cardId);
            return ResponseEntity
                    .created(uriBuilder.path("/api/boards/" + boardId + "/card-lists/" + cardListsId + "/cards/" + cardId + "/tasks/{taskId}")
                            .buildAndExpand(task.getId()).toUri())
                    .body(task);
        }
    }

    @Loggable
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("boardId") Long boardId,
                                           @PathVariable("taskId") Long taskId) {
        TaskDto task = taskService.findById(taskId, boardId);
        return ResponseEntity.ok(task);
    }

    @Loggable
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable("boardId") Long boardId,
                                                  @PathVariable("cardId") Long cardId) {
        List<TaskDto> allByCard = taskService.findAllByCard(cardId, boardId);
        return ResponseEntity.ok(allByCard);
    }

    @Loggable
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDto> completeTask(@PathVariable("taskId") Long taskId,
                                                @PathVariable("cardId") Long cardId) {
        TaskDto taskDto = taskService.update(taskId, cardId);
        return ResponseEntity.ok(taskDto);
    }

    @Loggable
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId,
                                           @PathVariable("boardId") Long boardId) {
        taskService.delete(taskId, boardId);
        return ResponseEntity.noContent().build();
    }
}
