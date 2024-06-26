package com.suhoi.demo.mapper;


import com.suhoi.demo.dto.BoardCreateDto;
import com.suhoi.demo.dto.BoardDto;
import com.suhoi.demo.dto.BoardUpdateDto;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.util.UserUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class BoardMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtils userUtils;

    @Mapping(target = "moderators", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "creator", ignore = true)
    public abstract Board map(BoardCreateDto dto);

    protected Set<User> mapModerators(BoardCreateDto dto) {
        Set<User> moderators = userRepository.findByEmailIn(dto.getModerators());
        moderators.add(userUtils.getCurrentUser());
        return moderators;
    }

    protected Set<User> mapMembers(BoardCreateDto dto) {
        Set<User> members = userRepository.findByEmailIn(dto.getMembers());
        members.add(userUtils.getCurrentUser());
        return members;
    }

    @AfterMapping
    protected void afterMappingCreate(@MappingTarget Board board, BoardCreateDto dto) {
        Set<User> moderators = userRepository.findByEmailIn(dto.getModerators());
        Set<User> members = userRepository.findByEmailIn(dto.getMembers());
        User currentUser = userUtils.getCurrentUser();
        moderators.add(currentUser);
        members.add(currentUser);
        board.setModerators(moderators);
        board.setMembers(members);
        board.setCreator(currentUser);
    }

    @Mapping(target = "moderators", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "creator", ignore = true)
    public abstract BoardDto map(Board board);

    @AfterMapping
    protected void afterMappingDto(@MappingTarget BoardDto dto, Board board) {
        Set<User> members = board.getMembers();
        Set<User> moderators = board.getModerators();
        User creator = board.getCreator();
        dto.setMembers(members.stream().map(User::getEmail).toList());
        dto.setModerators(moderators.stream().map(User::getEmail).toList());
        dto.setCreator(creator.getEmail());
    }

    public abstract void update(BoardUpdateDto dto, @MappingTarget Board board);
}
