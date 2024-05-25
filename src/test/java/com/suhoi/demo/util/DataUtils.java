package com.suhoi.demo.util;

import com.suhoi.demo.dto.CardDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.model.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DataUtils {

    public static TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Sample Task");
        taskDto.setDescription("This is a sample task description.");
        taskDto.setComplete(false);
        taskDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        taskDto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return taskDto;
    }

    public static Task getTaskPersist() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");
        task.setDescription("Sample Description");
        task.setCard(getCardPersist());
        return task;
    }

    public static CardDto getCardDto() {
        CardDto cardDto = new CardDto();
        cardDto.setTitle("Sample Title");
        cardDto.setDescription("Sample Description");
        cardDto.setStatus("NEW");
        cardDto.setImportance("High");
        cardDto.setAssignees(List.of("user1@example.com", "user2@example.com"));
        cardDto.setDeadline(LocalDate.now().plusDays(10));
        cardDto.setBurned(false);
        cardDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        cardDto.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return cardDto;
    }

    public static Card getCardPersist() {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Sample Card");
        card.setDescription("Sample Description");
        card.setCreator(getJohnPersist());
        card.setStatus(Status.NEW);
        card.setImportance("High");
        card.setAssignees(List.of(new User()));
        card.setDeadline(LocalDate.now());
        card.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        card.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        card.setCardList(getCardListPersist());
        return card;
    }

    public static CardList getCardListPersist() {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("Sample CardList");
        cardList.setDescription("Sample Description");
        cardList.setBoard(getBoardPersist());
        cardList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        cardList.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return cardList;
    }

    public static Card getCardTransient() {
        Card card = new Card();
        card.setTitle("Card Title");
        card.setDescription("Card Description");
        card.setCreator(getJohnTransient());
        card.setDeadline(LocalDate.now().plusDays(1));
        card.setCardList(getCardListTransient());
        card.setBurned(true);
        return card;
    }

    public static Task getTaskTransient() {
        Task task = new Task();
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setCreator(getJohnTransient());
        task.setCard(getCardTransient());
        return task;
    }

    public static Task getAnotherTaskTransient() {
        Task task = new Task();
        task.setTitle("Another Task Title");
        task.setDescription("Another Task Description");
        task.setCreator(getJohnTransient());
        task.setCard(getCardTransient());
        return task;
    }

    public static Card getAnotherCardTransient() {
        Card card = new Card();
        card.setTitle("Another Card Title");
        card.setDescription("Another Card Description");
        card.setCreator(getJohnTransient());
        card.setCardList(getCardListTransient());
        card.setDeadline(LocalDate.now().minusDays(1));
        card.setBurned(false);
        return card;
    }

    public static CardList getCardListTransient() {
        CardList cardList = new CardList();
        cardList.setTitle("CardList Title");
        cardList.setDescription("CardList Description");
        cardList.setCreator(DataUtils.getJohnTransient());
        cardList.setBoard(getBoardTransient());
        return cardList;
    }

    public static Board getBoardTransient() {
        Board board = new Board();
        board.setTitle("Another Board Title");
        board.setDescription("Another Board Description");
        Set<User> members = new HashSet<>();
        members.add(DataUtils.getJohnTransient());
        board.setMembers(members);
        Set<User> moderators = new HashSet<>();
        moderators.add(DataUtils.getJohnTransient());
        board.setModerators(moderators);
        board.setCreator(DataUtils.getJohnTransient());
        return board;
    }

    public static Board getAnotherBoardTransient() {
        Board board = new Board();
        board.setTitle("Another Board Title");
        board.setDescription("Another Board Description");
        Set<User> members = new HashSet<>();
        members.add(DataUtils.getJohnTransient());
        board.setMembers(members);
        Set<User> moderators = new HashSet<>();
        moderators.add(DataUtils.getJohnTransient());
        board.setModerators(moderators);
        board.setCreator(DataUtils.getJohnTransient());
        return board;
    }

    public static Board getBoardPersist() {
        Board board = new Board();
        board.setId(1L);
        board.setTitle("Board Title");
        board.setDescription("Board Description");
        board.setMembers(new HashSet<>());
        board.setModerators(new HashSet<>());
        board.setCreator(getJohnPersist());
        return board;
    }

    public static User getJohnTransient() {
        User user = new User();
        user.setUsername("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password1");
        return user;
    }

    public static User getJohnPersist() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Johns");
        user.setEmail("john@gmail.com");
        user.setPassword("password1");
        return user;
    }

    public static User getMikeTransient() {
        User user = new User();
        user.setUsername("Mike");
        user.setEmail("mike@gmail.com");
        user.setPassword("password2");
        return user;
    }

    public static User getKiraTransient() {
        User user = new User();
        user.setUsername("Kira");
        user.setEmail("kira@gmail.com");
        user.setPassword("password3");
        return user;
    }

}
