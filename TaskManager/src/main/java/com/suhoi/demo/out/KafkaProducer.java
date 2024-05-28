package com.suhoi.demo.out;

import com.suhoi.demo.out.event.UserActionEvent;

public interface KafkaProducer {
    void sendUserAction(UserActionEvent userActionEvent);
}
