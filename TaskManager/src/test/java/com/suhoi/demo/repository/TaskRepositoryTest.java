package com.suhoi.demo.repository;

import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.Task;
import org.springframework.boot.test.context.SpringBootTest;

import com.suhoi.demo.container.PostgresContainer;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskRepositoryTest extends PostgresContainer {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        System.out.println("delete all");
    }

    @Test
    @DisplayName("Test save task functionality")
    public void givenTask_whenSave_thenTaskIsSaved() {
        //given
        Task taskToSave = DataUtils.getTaskTransient();

        //when
        Task savedTask = taskRepository.save(taskToSave);

        //then
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test find task by id functionality")
    public void givenTaskId_whenFindById_thenTaskIsFound() {
        //given
        Task savedTask = taskRepository.save(DataUtils.getTaskTransient());

        //when
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        //then
        assertThat(foundTask).isPresent();
    }

    @Test
    @DisplayName("Test update task functionality")
    public void givenUpdatedTask_whenSave_thenTaskIsUpdated() {
        //given
        Task savedTask = taskRepository.save(DataUtils.getTaskTransient());

        String updatedTitle = "Updated Title";
        savedTask.setTitle(updatedTitle);

        //when
        Task updatedTask = taskRepository.save(savedTask);

        //then
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("Test delete task functionality")
    public void givenTaskId_whenDelete_thenTaskIsDeleted() {
        //given
        Task savedTask = taskRepository.save(DataUtils.getTaskTransient());

        //when
        taskRepository.deleteById(savedTask.getId());

        //then
        assertThat(taskRepository.findById(savedTask.getId())).isEmpty();
    }

    @Test
    @DisplayName("Test find tasks by card id functionality")
    public void givenCardId_whenFindTasksByCardId_thenTasksReturned() {
        //given
        Task task1 = DataUtils.getTaskTransient();
        Task task2 = DataUtils.getAnotherTaskTransient();
        task1.setCard(DataUtils.getCardTransient());
        task2.setCard(DataUtils.getCardTransient());
        task2.setCard(task1.getCard());
        taskRepository.saveAll(List.of(task1, task2));

        //when
        List<Task> tasks = taskRepository.findTasksByCardId(task1.getCard().getId());

        //then
        assertThat(tasks).isNotEmpty();
        assertThat(tasks.size()).isEqualTo(2);
    }
}

