package com.suhoi.audit.service.impl;

import com.suhoi.audit.in.handler.event.UserActionEvent;
import com.suhoi.audit.model.Audit;
import com.suhoi.audit.repository.AuditRepository;
import com.suhoi.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public void save(UserActionEvent userActionEvent) {
        Audit audit = Audit.builder()
                .uuid(UUID.randomUUID())
                .username(userActionEvent.getUsername())
                .action(userActionEvent.getAction())
                .timestamp(userActionEvent.getTimestamp())
                .build();
        auditRepository.save(audit);
    }

    public List<Audit> findAll() {
        return auditRepository.findAll();
    }
}
