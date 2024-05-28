package com.suhoi.demo.repository;

import com.suhoi.demo.model.Card;
import org.springframework.boot.test.context.SpringBootTest;

import com.suhoi.demo.container.PostgresContainer;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardRepositoryTest extends PostgresContainer {

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        cardRepository.deleteAll();
        System.out.println("delete all");
    }

    @Test
    @DisplayName("Test save card functionality")
    public void givenCard_whenSave_thenCardIsSaved() {
        //given
        Card cardToSave = DataUtils.getCardTransient();

        //when
        Card savedCard = cardRepository.save(cardToSave);

        //then
        assertThat(savedCard).isNotNull();
        assertThat(savedCard.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test find card by id functionality")
    public void givenCardId_whenFindById_thenCardIsFound() {
        //given
        Card savedCard = cardRepository.save(DataUtils.getCardTransient());

        //when
        Optional<Card> foundCard = cardRepository.findById(savedCard.getId());

        //then
        assertThat(foundCard).isPresent();
    }

    @Test
    @DisplayName("Test update card functionality")
    public void givenUpdatedCard_whenSave_thenCardIsUpdated() {
        //given
        Card savedCard = cardRepository.save(DataUtils.getCardTransient());

        String updatedTitle = "Updated Title";
        savedCard.setTitle(updatedTitle);

        //when
        Card updatedCard = cardRepository.save(savedCard);

        //then
        assertThat(updatedCard).isNotNull();
        assertThat(updatedCard.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("Test delete card functionality")
    public void givenCardId_whenDelete_thenCardIsDeleted() {
        //given
        Card savedCard = cardRepository.save(DataUtils.getCardTransient());

        //when
        cardRepository.deleteById(savedCard.getId());

        //then
        assertThat(cardRepository.findById(savedCard.getId())).isEmpty();
    }

    @Test
    @DisplayName("Test find cards by card list id functionality")
    public void givenCardListId_whenFindCardsByCardListId_thenCardsReturned() {
        //given
        Card card1 = DataUtils.getCardTransient();
        Card card2 = DataUtils.getAnotherCardTransient();
        card1.setCardList(DataUtils.getCardListTransient());
        card2.setCardList(DataUtils.getCardListTransient());
        card2.setCardList(card1.getCardList());
        cardRepository.saveAll(List.of(card1, card2));

        //when
        List<Card> cards = cardRepository.findCardsByCardListId(card1.getCardList().getId());

        //then
        assertThat(cards).isNotEmpty();
        assertThat(cards.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test find card by id and card list id functionality")
    public void givenCardIdAndCardListId_whenFindCardByIdAndCardListId_thenCardIsFound() {
        //given
        Card savedCard = cardRepository.save(DataUtils.getCardTransient());

        //when
        Optional<Card> foundCard = cardRepository.findCardByIdAndCardListId(savedCard.getId(), savedCard.getCardList().getId());

        //then
        assertThat(foundCard).isPresent();
    }

    @Test
    @DisplayName("Test find cards by deadline before functionality")
    public void givenDeadline_whenFindCardsByDeadlineBefore_thenCardsReturned() {
        //given
        Card card1 = DataUtils.getCardTransient();
        Card card2 = DataUtils.getAnotherCardTransient();
        cardRepository.saveAll(List.of(card1, card2));

        //when
        List<Card> cards = cardRepository.findCardByDeadlineBefore(LocalDate.now());

        //then
        assertThat(cards).isNotEmpty();
        assertThat(cards.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test find cards by card list id and burned functionality")
    public void givenCardListIdAndBurned_whenFindCardsByCardListIdAndBurned_thenCardsReturned() {
        //given

        Card card1 = DataUtils.getCardTransient();
        Card card2 = DataUtils.getAnotherCardTransient();
        card2.setCardList(card1.getCardList());
        cardRepository.saveAll(List.of(card1, card2));

        //when
        List<Card> cards = cardRepository.findCardsByCardListIdAndBurned(card1.getCardList().getId(), true);

        //then
        assertThat(cards).isNotEmpty();
        assertThat(cards.size()).isEqualTo(1);
    }
}


