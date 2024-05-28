package com.suhoi.demo.mapper;

import com.suhoi.demo.model.ParentForJsonOfNullable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
@RequiredArgsConstructor
public abstract class ReferenceMapper {
    private EntityManager entityManager;

    public <T extends ParentForJsonOfNullable> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }
}
