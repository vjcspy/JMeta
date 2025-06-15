package com.vjcspy.stockinfo.domain.tick;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface TickMapper {

    TickDto toDto(TickEntity entity);

    @Mapping(target = "id", ignore = true)
    TickEntity toEntity(TickDto dto);
}
