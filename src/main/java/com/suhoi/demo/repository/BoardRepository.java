package com.suhoi.demo.repository;

import com.suhoi.demo.model.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
    @Query("select distinct b from Board b left join fetch b.moderators left join fetch b.members where :userId in (select um.id from b.members um)")
    List<Board> findBoardsByMembersId(@Param("userId") Long userId);

    List<Board> findBoardsByCreatorId(@Param("userId") Long userId);

    @Query("select b from Board b join b.members m where m.id = :userId and b.id = :id")
    Optional<Board> findByIdAndUserId(@Param("userId") Long userId, @Param("id") Long id);
}
