package com.suhoi.audit.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AuditCreateDto {
    private String username;
    private String action;
    private Timestamp timestamp;
}
