package com.suhoi.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CardCreateDto {
    private String title;
    private String description;
    private String importance;
    private List<String> assignees;
    private LocalDate deadline;
}
