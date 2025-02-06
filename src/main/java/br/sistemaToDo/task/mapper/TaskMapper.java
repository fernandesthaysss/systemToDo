package br.sistemaToDo.task.mapper;

import br.sistemaToDo.task.dto.TaskRequestDTO;
import br.sistemaToDo.task.dto.TaskResponseDTO;
import br.sistemaToDo.task.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "category", target = "category")
    TaskEntity toEntity(TaskRequestDTO taskRequestDTO);

    @Mapping(source = "category", target = "category")
    TaskResponseDTO toResponse(TaskEntity taskEntity);

}