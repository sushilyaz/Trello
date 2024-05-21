package com.suhoi.demo.in.controller;

import com.suhoi.demo.service.BoardService;
import com.suhoi.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/card-lists/{cardListsId}/cards/{cardId}/tasks")
public class TaskController {

    private final TaskService taskService;
}
