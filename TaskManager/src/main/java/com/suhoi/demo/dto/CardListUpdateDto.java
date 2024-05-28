package com.suhoi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardListUpdateDto {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
}
