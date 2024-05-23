package com.suhoi.demo.mapper;

import com.suhoi.demo.dto.TaskCreateDto;
import com.suhoi.demo.dto.TaskDto;
import com.suhoi.demo.model.Task;
import com.suhoi.demo.repository.CardRepository;
import com.suhoi.demo.util.UserUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private UserUtils userUtils;

    public abstract Task map(TaskCreateDto dto);

    public abstract TaskDto map(Task task);

    @AfterMapping
    protected void afterMapping(@MappingTarget Task task, TaskCreateDto dto) {
        task.setComplete(false);
        task.setCreator(userUtils.getCurrentUser());

    }
}
