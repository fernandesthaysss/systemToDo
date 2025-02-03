package com.example.task.mapper;

import com.example.task.dto.TaskRequestDTO;
import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "category", target = "category")
    TaskEntity toEntity(TaskRequestDTO taskRequestDTO);

    @Mapping(source = "category", target = "category")
    TaskResponseDTO toResponse(TaskEntity taskEntity);
}