package com.vjcspy.stockinfo.model.tick;

public class TickEntityMapper {

    public static TickEntityDto toDto(TickEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TickEntityDto(
            entity.id,  // No conversion needed now
            entity.symbol,
            entity.date,
            entity.meta
        );
    }

    public static TickEntity toEntity(TickEntityDto dto) {
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
