package com.suhoi.audit.in.handler;

import com.suhoi.audit.in.handler.event.UserActionEvent;
import com.suhoi.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final AuditService auditService;

    @KafkaListener(topics = "user-action", groupId = "user-action-events")
    public void listener(UserActionEvent userActionEvent) {
        auditService.save(userActionEvent);
        System.out.println(userActionEvent.toString());

    }
}
