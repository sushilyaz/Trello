package com.suhoi.demo.dto;

import com.suhoi.demo.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BoardCreateDto {

    @NotBlank
    private String title;
    private String description;
    private List<String> moderators;
    private List<String> members;
}
