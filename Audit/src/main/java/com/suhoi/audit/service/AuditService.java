package com.suhoi.audit.service;

import com.suhoi.audit.in.handler.event.UserActionEvent;
import com.suhoi.audit.model.Audit;

import java.util.List;

public interface AuditService {

    void save(UserActionEvent userActionEvent);

    List<Audit> findAll();
}
