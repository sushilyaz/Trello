package com.suhoi.demo.dto;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Data
public class BoardUpdateDto {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<List<String>> moderators;
    private JsonNullable<List<String>> members;
}
