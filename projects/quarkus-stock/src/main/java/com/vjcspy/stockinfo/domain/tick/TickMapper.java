package com.vjcspy.stockinfo.domain.tick;

public class TickMapper {

    public static TickDto toDto(TickEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TickDto(
            entity.id,
            entity.symbol,
            entity.date,
            entity.meta
        );
    }

    public static TickEntity toEntity(TickDto dto) {
        if (dto == null) {
            return null;
        }

        TickEntity entity = new TickEntity();
        entity.id = dto.getId();
        entity.symbol = dto.getSymbol();
        entity.date = dto.getDate();
        entity.meta = dto.getMeta();

        return entity;
    }
}
