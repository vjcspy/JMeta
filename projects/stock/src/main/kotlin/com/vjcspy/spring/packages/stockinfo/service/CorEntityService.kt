/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.service

import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto
import com.vjcspy.spring.packages.stockinfo.mapper.CorMapper
import com.vjcspy.spring.packages.stockinfo.repository.CorEntityRepository
import org.springframework.stereotype.Service

@Service
class CorEntityService(private val corEntityRepository: CorEntityRepository) {
    /**
     * Lấy danh sách tất cả CorEntity và chuyển sang CorEntityDto
     *
     * @return List<CorEntityDto>
     */
    fun getAllCorEntities(): List<CorEntityDto> {
        val entities = corEntityRepository.findAll()
        return entities.map(CorMapper.INSTANCE::toDto)
    }
}
