package com.vjcspy.stockinfo.model.tick;

public class TickMapper {

    public static TickDto toDto(TickEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TickDto(
            entity.id,  // No conversion needed now
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
        entity.id = dto.getId();  // No conversion needed now
        entity.symbol = dto.getSymbol();
        entity.date = dto.getDate();
        entity.meta = dto.getMeta();

        return entity;
    }
}
