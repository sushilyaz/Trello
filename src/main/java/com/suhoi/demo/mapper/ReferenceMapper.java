package com.suhoi.demo.mapper;

import com.suhoi.demo.model.BaseEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;

//@Mapper(
//        componentModel = MappingConstants.ComponentModel.SPRING
//)
//@RequiredArgsConstructor
//public abstract class ReferenceMapper {
//
//    private final EntityManager entityManager;
//
//    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
//        return id != null ? entityManager.find(entityClass, id) : null;
//    }
//}
