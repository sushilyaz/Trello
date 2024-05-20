package com.suhoi.demo.repository;

import com.suhoi.demo.model.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
    @EntityGraph(value = "GetModeratorsAndMembers", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Board b join b.members m where m.id = :userId")
    List<Board> findBoardsByMembersId(@Param("userId") Long userId);

    List<Board> findBoardsByCreatorId(@Param("userId") Long userId);

}
