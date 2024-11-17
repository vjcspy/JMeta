// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stockinfo.repository

import com.vjcspy.spring.packages.stockinfo.entity.CorEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CorEntityRepository : JpaRepository<CorEntity, Int> {
    fun findByCode(code: String): Optional<CorEntity>
}
