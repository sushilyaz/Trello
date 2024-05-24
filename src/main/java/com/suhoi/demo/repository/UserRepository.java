package com.suhoi.demo.repository;

import com.suhoi.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value = "GetModeratorsAndMembers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);
    Set<User> findByEmailIn(List<String> emails);
}
