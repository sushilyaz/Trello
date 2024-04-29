package com.suhoi.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {

    private String title;

    private String description;

    private List<String> moderators;

    private List<String> members;

    private String creator;
}
