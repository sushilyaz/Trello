package com.suhoi.demo.scheduler;

import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardScheduler {

    private final CardRepository cardRepository;

    @Scheduled(cron = "0 1 0 * * ?")
    public void scheduledTask() {
        LocalDate today = LocalDate.now();
        List<Card> cards = cardRepository.findCardByDeadlineBefore(today);
        for (Card card : cards) {
            List<Task> tasks = card.getTask();
            for (Task task : tasks) {
                if (!task.isComplete()) {
                    card.setBurned(true);
                    break;
                }
            }
        }
    }
}
