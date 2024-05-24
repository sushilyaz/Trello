package com.suhoi.demo.mapper;

import com.suhoi.demo.dto.*;
import com.suhoi.demo.model.CardList;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CardListMapper {

    public abstract CardListDto map(CardList cardList);
    public abstract CardList map(CardListCreateDto dto);
    public abstract void update(CardListUpdateDto dto, @MappingTarget CardList cardList);
}
