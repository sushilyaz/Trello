package com.suhoi.demo.dto;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.List;

@Data
public class CardUpdateDto {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<String> importance;
    private JsonNullable<List<String>> assignees;
    private JsonNullable<LocalDate> deadline;
}
