package com.suhoi.demo.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CardListDto {
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
