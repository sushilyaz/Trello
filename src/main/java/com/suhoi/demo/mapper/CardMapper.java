package com.suhoi.demo.mapper;

import com.suhoi.demo.dto.CardCreateDto;
import com.suhoi.demo.dto.CardUpdateDto;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.model.Card;
import com.suhoi.demo.model.Status;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.UserRepository;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CardMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Mapping(target = "assignees", source = "assignees")
    public abstract Card map(CardCreateDto dto);

    @Mapping(target = "assignees", source = "assignees")
    public abstract void update(@MappingTarget Card card, CardUpdateDto dto);

    @IterableMapping(qualifiedByName = "mapEmailToUser")
    protected abstract List<User> mapEmailsToUsers(List<String> emails);

    @Named("mapEmailToUser")
    protected User mapEmailToUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User with email " + email + " not found"));
    }

    @AfterMapping
    protected void afterMapping(@MappingTarget Card card, CardUpdateDto dto) {
        if (dto.getAssignees() != null) {
            card.setAssignees(mapEmailsToUsers(dto.getAssignees().get()));
        }
    }

    @AfterMapping
    protected void afterMappingCreate(@MappingTarget Card card, CardCreateDto dto) {
        List<User> assignees = new ArrayList<>();
        dto.getAssignees().forEach(assignee -> {
            Optional<User> user = userRepository.findByEmail(assignee);
            user.ifPresent(assignees::add);
        });
        card.setCreator(userUtils.getCurrentUser());
        card.setAssignees(assignees);
        card.setStatus(Status.NEW);
    }
}
