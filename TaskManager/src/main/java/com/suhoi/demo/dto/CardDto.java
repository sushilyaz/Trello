package com.suhoi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private String title;
    private String description;
    private String status;
    private String importance;
    private List<String> assignees;
    private LocalDate deadline;
    private boolean burned;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
