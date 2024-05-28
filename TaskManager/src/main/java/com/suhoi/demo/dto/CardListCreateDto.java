package com.suhoi.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CardListCreateDto {
    @NotBlank
    private String title;
    private String description;
}
