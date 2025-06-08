package com.vjcspy.stockinfo.model.corentity;

public class CorEntityMapper {

    public static CorEntityDTO toDTO(CorEntity entity) {
        CorEntityDTO dto = new CorEntityDTO();
        dto.id = entity.id;
        dto.code = entity.code;
        dto.exchange = entity.exchange;
        dto.name = entity.name;
        dto.refId = entity.refId;
        dto.catId = entity.catId;
        dto.industryName1 = entity.industryName1;
        dto.industryName2 = entity.industryName2;
        dto.industryName3 = entity.industryName3;
        dto.totalShares = entity.totalShares;
        dto.firstTradeDate = entity.firstTradeDate;
        return dto;
    }

    public static CorEntity toEntity(CorEntityDTO dto) {
        CorEntity entity = new CorEntity();
        entity.id = dto.id;
        entity.code = dto.code;
        entity.exchange = dto.exchange;
        entity.name = dto.name;
        entity.refId = dto.refId;
        entity.catId = dto.catId;
        entity.industryName1 = dto.industryName1;
        entity.industryName2 = dto.industryName2;
        entity.industryName3 = dto.industryName3;
        entity.totalShares = dto.totalShares;
        entity.firstTradeDate = dto.firstTradeDate;
        return entity;
    }
}
