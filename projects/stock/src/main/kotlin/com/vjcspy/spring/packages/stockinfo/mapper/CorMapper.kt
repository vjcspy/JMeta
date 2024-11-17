// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stockinfo.mapper

import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto
import com.vjcspy.spring.packages.stockinfo.entity.CorEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface CorMapper {
    companion object {
        val INSTANCE: CorMapper = Mappers.getMapper(CorMapper::class.java)
    }

    // Mapping từ Entity sang DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "refId", target = "refId")
    @Mapping(source = "catId", target = "catId")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "exchange", target = "exchange")
    @Mapping(source = "industryName1", target = "industryName1")
    @Mapping(source = "industryName2", target = "industryName2")
    @Mapping(source = "industryName3", target = "industryName3")
    @Mapping(source = "totalShares", target = "totalShares")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "firstTradeDate", target = "firstTradeDate")
    fun toDto(entity: CorEntity): CorEntityDto

    // Mapping từ DTO sang Entity
    @Mapping(source = "id", target = "id")
    @Mapping(source = "refId", target = "refId")
    @Mapping(source = "catId", target = "catId")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "exchange", target = "exchange")
    @Mapping(source = "industryName1", target = "industryName1")
    @Mapping(source = "industryName2", target = "industryName2")
    @Mapping(source = "industryName3", target = "industryName3")
    @Mapping(source = "totalShares", target = "totalShares")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "firstTradeDate", target = "firstTradeDate")
    fun toEntity(dto: CorEntityDto): CorEntity
}
