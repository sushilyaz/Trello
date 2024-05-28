package com.suhoi.demo.scheduler;

import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@EnableScheduling
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class CardSchedulerTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardScheduler cardScheduler;

    @Test
    public void testScheduledTask() {
        // given
        LocalDate today = LocalDate.now();
        Card card1 = new Card();
        card1.setDeadline(today.minusDays(1));
        Task task1 = new Task();
        task1.setComplete(false);
        card1.setTask(Collections.singletonList(task1));

        Card card2 = new Card();
        card2.setDeadline(today.minusDays(1));
        Task task2 = new Task();
        task2.setComplete(true);
        card2.setTask(Collections.singletonList(task2));

        List<Card> cards = Arrays.asList(card1, card2);

        BDDMockito.given(cardRepository.findCardByDeadlineBefore(any(LocalDate.class))).willReturn(cards);

        // when
        cardScheduler.scheduledTask();

        // then
        verify(cardRepository).findCardByDeadlineBefore(any(LocalDate.class));

        assertThat(card1.isBurned()).isTrue();

        assertThat(card2.isBurned()).isFalse();
    }
}

