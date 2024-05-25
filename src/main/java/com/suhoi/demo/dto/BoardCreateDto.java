package com.suhoi.demo.dto;

import com.suhoi.demo.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateDto {

    @NotBlank
    private String title;
    private String description;
    private List<String> moderators;
    private List<String> members;
}
