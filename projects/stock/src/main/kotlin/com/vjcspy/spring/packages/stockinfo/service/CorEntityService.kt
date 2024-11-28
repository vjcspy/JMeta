// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stockinfo.service

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto
import com.vjcspy.spring.packages.stockinfo.entity.CorEntity
import com.vjcspy.spring.packages.stockinfo.mapper.CorMapper
import com.vjcspy.spring.packages.stockinfo.repository.CorEntityRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CorEntityService(
    private val corEntityRepository: CorEntityRepository,
) {
    private val logger = KtLogging.logger()

    /**
     * Lấy danh sách tất cả CorEntity và chuyển sang CorEntityDto
     *
     * @return List<CorEntityDto>
     */
    fun getAllCorEntities(): List<CorEntityDto> {
        val entities = corEntityRepository.findAll()
        return entities.map(CorMapper.INSTANCE::toDto)
    }

    @Transactional
    fun saveCorEntity(corEntity: CorEntity): CorEntity = corEntityRepository.save(corEntity)

    @Transactional
    fun saveCorEntities(corEntities: List<CorEntity>): List<CorEntity> {
        logger.info("Saving cor entities")
        return corEntityRepository.saveAll(corEntities)
    }

    @Transactional
    fun deleteAllCorEntities() {
        logger.info("Deleting all cor entities")
        return corEntityRepository.deleteAll()
    }
}
