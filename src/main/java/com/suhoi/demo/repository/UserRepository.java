package com.suhoi.demo.repository;

import com.suhoi.demo.model.User;
import jakarta.persistence.NamedEntityGraph;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value = "GetModeratorsAndMembers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String username);
    List<User> findByEmailIn(List<String> emails);
}
