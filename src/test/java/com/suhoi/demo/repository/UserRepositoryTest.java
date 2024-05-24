package com.suhoi.demo.repository;

import com.suhoi.demo.container.PostgresContainer;
import com.suhoi.demo.model.User;
import com.suhoi.demo.util.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest extends PostgresContainer {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        System.out.println("delete all");
    }

    @Test
    @DisplayName("Test save user functionality")
    public void givenUser_whenSave_thenUserIsSaved() {
        //given
        User userToSave = DataUtils.getJohnTransient();
        //when
        User savedUser = userRepository.save(userToSave);
        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test findAll user functionality")
    public void givenUsers_whenSave_thenUsersReturned() {
        //given
        User user1 = DataUtils.getJohnTransient();
        User user2 = DataUtils.getMikeTransient();
        User user3 = DataUtils.getKiraTransient();
        userRepository.saveAll(List.of(user1, user2, user3));
        //when
        List<User> users = userRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(users)).isFalse();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test findById user functionality. User is found")
    public void givenUser_whenFindById_thenUserIsFound() {
        //given
        User userToSave = DataUtils.getJohnTransient();
        userRepository.save(userToSave);
        //when
        User user = userRepository.findById(userToSave.getId()).orElse(null);
        //then
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Test findById user functionality. User is not found")
    public void givenUser_whenFindById_thenUserIsNotFound() {
        //given
        User userToSave = DataUtils.getJohnPersist();
        //when
        User user = userRepository.findById(userToSave.getId()).orElse(null);
        //then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Test findByEmail user functionality. User is found")
    public void givenUser_whenFindByEmail_thenUserIsFound() {
        //given
        User userToSave = DataUtils.getJohnTransient();
        userRepository.save(userToSave);
        //when
        User user = userRepository.findByEmail(userToSave.getEmail()).orElse(null);
        //then
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Test findByEmail user functionality. User is not found")
    public void givenUser_whenFindByEmail_thenUserIsNotFound() {
        //given

        //when
        User user = userRepository.findByEmail("john@gmail.com").orElse(null);
        //then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Test findByEmailIn functionality")
    public void givenUsersSaved_whenFindByEmailIn_thenUserIsFound() {
        //given
        User user1 = DataUtils.getJohnTransient();
        User user2 = DataUtils.getMikeTransient();
        User user3 = DataUtils.getKiraTransient();
        userRepository.saveAll(List.of(user1, user2, user3));
        //when
        List<String> emails = List.of(user1.getEmail(), user2.getEmail(), user3.getEmail());
        Set<User> users = userRepository.findByEmailIn(emails);
        //then
        assertThat(CollectionUtils.isEmpty(users)).isFalse();
    }
}
