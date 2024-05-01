package com.suhoi.demo.mapper;


import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class BoardMapper {

    @Mapping(target = "moderators", source = "moderators")
    @Mapping(target = "members", source = "members")
    public abstract Board map(BoardCreateDto dto, List<User> moderators, List<User> members);

   // @Mapping(target = "moderators", source = "moderators")
    //@Mapping(target = "members", source = "members")
    //public abstract BoardDto map(Board board, List<User> moderators, List<User> members);

    @Mapping(target = "moderators", source = "moderators")
    @Mapping(target = "members", source = "members")
    public abstract void update(BoardUpdateDto dto, List<User> moderators, List<User> members, @MappingTarget Board board);
}
