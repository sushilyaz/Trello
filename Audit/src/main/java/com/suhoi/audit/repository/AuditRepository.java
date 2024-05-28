package com.suhoi.audit.repository;

import com.suhoi.audit.model.Audit;

import java.util.List;


public interface AuditRepository {

    Audit save(Audit audit);

    List<Audit> findAll();
}

