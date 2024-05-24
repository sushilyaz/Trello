package com.suhoi.demo.repository;

import com.suhoi.demo.container.PostgresContainer;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardListRepositoryTest extends PostgresContainer {

    @Autowired
    private CardListRepository cardListRepository;

    @BeforeEach
    public void setUp() {
        cardListRepository.deleteAll();
        System.out.println("delete all");
    }

    @Test
    @DisplayName("Test save card list functionality")
    public void givenCardList_whenSave_thenCardListIsSaved() {
        //given
        CardList cardListToSave = DataUtils.getCardListTransient();

        //when
        CardList savedCardList = cardListRepository.save(cardListToSave);

        //then
        assertThat(savedCardList).isNotNull();
        assertThat(savedCardList.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test find card list by id functionality")
    public void givenCardListId_whenFindById_thenCardListIsFound() {
        //given
        CardList cardListToSave = DataUtils.getCardListTransient();
        CardList savedCardList = cardListRepository.save(cardListToSave);

        //when
        Optional<CardList> foundCardList = cardListRepository.findById(savedCardList.getId());

        //then
        assertThat(foundCardList).isPresent();
    }

    @Test
    @DisplayName("Test find card list by board id functionality")
    public void givenBoardId_whenFindByBoardId_thenCardListsReturned() {
        //given

        CardList cardList1 = DataUtils.getCardListTransient();
        CardList cardList2 = DataUtils.getCardListTransient();
        cardList2.setBoard(cardList1.getBoard());

        cardListRepository.saveAll(List.of(cardList1, cardList2));
        Long id = cardList1.getBoard().getId();

        //when
        List<CardList> cardLists = cardListRepository.findByBoardId(id);

        //then
        assertThat(cardLists).isNotEmpty();
        assertThat(cardLists.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test find card list by board id and list id functionality")
    public void givenBoardIdAndListId_whenFindByBoardIdAndId_thenCardListIsFound() {
        //given
        CardList cardListToSave = DataUtils.getCardListTransient();
        CardList savedCardList = cardListRepository.save(cardListToSave);
        Long id = savedCardList.getBoard().getId();
        //when
        Optional<CardList> foundCardList = cardListRepository.findByBoardIdAndId(id, savedCardList.getId());

        //then
        assertThat(foundCardList).isPresent();
    }

    @Test
    @DisplayName("Test update card list functionality")
    public void givenUpdatedCardList_whenSave_thenCardListIsUpdated() {
        //given
        CardList cardListToSave = DataUtils.getCardListTransient();
        CardList savedCardList = cardListRepository.save(cardListToSave);

        String updatedTitle = "Updated Title";
        savedCardList.setTitle(updatedTitle);

        //when
        CardList updatedCardList = cardListRepository.save(savedCardList);

        //then
        assertThat(updatedCardList).isNotNull();
        assertThat(updatedCardList.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("Test delete card list functionality")
    public void givenCardListId_whenDelete_thenCardListIsDeleted() {
        //given
        CardList cardListToSave = DataUtils.getCardListTransient();
        CardList savedCardList = cardListRepository.save(cardListToSave);

        //when
        cardListRepository.deleteById(savedCardList.getId());

        //then
        assertThat(cardListRepository.findById(savedCardList.getId())).isEmpty();
    }
}

