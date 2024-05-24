package com.suhoi.demo.util;

import com.suhoi.demo.model.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class DataUtils {

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
