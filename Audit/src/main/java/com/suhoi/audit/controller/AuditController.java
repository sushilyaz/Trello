package com.suhoi.audit.controller;

import com.suhoi.audit.model.Audit;
import com.suhoi.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/getAll")
    public List<Audit> getAllEntities() {
        return auditService.findAll();
    }
}
