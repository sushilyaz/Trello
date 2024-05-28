package com.suhoi.demo.mapper;

import com.suhoi.demo.dto.*;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.util.UserUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CardListMapper {

    @Autowired
    private UserUtils userUtils;

    public abstract CardListDto map(CardList cardList);
    public abstract CardList map(CardListCreateDto dto);
    public abstract void update(CardListUpdateDto dto, @MappingTarget CardList cardList);

    @AfterMapping
    protected void afterMapping(CardListCreateDto dto, @MappingTarget CardList cardList) {
        cardList.setCreator(userUtils.getCurrentUser());
    }
}
