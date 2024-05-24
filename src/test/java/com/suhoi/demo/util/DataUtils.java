package com.suhoi.demo.util;

import com.suhoi.demo.model.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DataUtils {

    public static Task getTaskPersist() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");
        task.setDescription("Sample Description");
        task.setCard(getCardPersist());
        return task;
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
        return Card.builder()
                .title("Card Title")
                .description("Card Description")
                .creator(getJohnTransient())
                .deadline(LocalDate.now().plusDays(1))
                .cardList(getCardListTransient())
                .burned(true)
                .build();
    }

    public static Task getTaskTransient() {
        return Task.builder()
                .title("Task Title")
                .description("Task Description")
                .creator(getJohnTransient())
                .card(getCardTransient())
                .build();
    }

    public static Task getAnotherTaskTransient() {
        return Task.builder()
                .title("Another Task Title")
                .description("Another Task Description")
                .creator(getJohnTransient())
                .card(getCardTransient())
                .build();
    }

    public static Card getAnotherCardTransient() {
        return Card.builder()
                .title("Another Card Title")
                .description("Another Card Description")
                .creator(getJohnTransient())
                .deadline(LocalDate.now().minusDays(1))
                .cardList(getCardListTransient())
                .burned(false)
                .build();
    }

    public static CardList getCardListTransient() {
        return CardList.builder()
                .title("CardList Title")
                .description("CardList Description")
                .creator(DataUtils.getJohnTransient())
                .board(getBoardTransient())
                .build();
    }

    public static CardList getAnotherCardListTransient() {
        return CardList.builder()
                .title("Another CardList Title")
                .description("Another CardList Description")
                .creator(DataUtils.getJohnTransient())
                .board(getBoardTransient())
                .build();
    }

    public static Board getBoardTransient() {
        return Board.builder()
                .title("Another Board Title")
                .description("Another Board Description")
                .members(Set.of(DataUtils.getJohnTransient()))
                .moderators(Set.of(DataUtils.getJohnTransient()))
                .creator(DataUtils.getJohnTransient())
                .build();
    }

    public static Board getAnotherBoardTransient() {
        return Board.builder()
                .title("Another Board Title")
                .description("Another Board Description")
                .members(Set.of(DataUtils.getJohnTransient()))
                .moderators(Set.of(DataUtils.getJohnTransient()))
                .creator(DataUtils.getJohnTransient())
                .build();
    }

    public static Board getBoardPersist() {
        return Board.builder()
                .id(1L)
                .title("Board Title")
                .description("Board Description")
                .members(new HashSet<>())
                .moderators(new HashSet<>())
                .creator(getJohnPersist())
                .build();
    }

    public static User getJohnTransient() {
        return User.builder()
                .username("John")
                .email("john@gmail.com")
                .password("password1")
                .build();
    }

    public static User getJohnPersist() {
        return User.builder()
                .id(1L)
                .username("Johns")
                .email("john@gmail.com")
                .password("password1")
                .build();
    }


    public static User getMikeTransient() {
        return User.builder()
                .username("Mike")
                .email("mike@gmail.com")
                .password("password2")
                .build();
    }

    public static User getKiraTransient() {
        return User.builder()
                .username("Kira")
                .email("kira@gmail.com")
                .password("password3")
                .build();
    }
}
