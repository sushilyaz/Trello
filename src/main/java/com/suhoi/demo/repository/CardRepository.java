package com.suhoi.demo.repository;

import com.suhoi.demo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findCardsByCardListId(Long cardListId);

    Optional<Card> findCardByIdAndCardListId(Long id, Long cardListId);
}
