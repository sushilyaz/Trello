package com.suhoi.demo.out.impl;

import com.suhoi.demo.out.event.UserActionEvent;
import com.suhoi.demo.out.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, UserActionEvent> kafkaTemplate;

    @Override
    public void sendUserAction(UserActionEvent userActionEvent) {
        kafkaTemplate.send("user-action", userActionEvent);
    }
}
