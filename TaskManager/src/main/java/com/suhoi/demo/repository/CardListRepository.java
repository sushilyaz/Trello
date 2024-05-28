package com.suhoi.demo.repository;

import com.suhoi.demo.model.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {
    List<CardList> findByBoardId(Long boardId);
    Optional<CardList> findByBoardIdAndId(Long boardId, Long id);
}
