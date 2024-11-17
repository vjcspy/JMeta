// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stockinfo.controller

import com.vjcspy.spring.base.dto.response.OkResponse
import com.vjcspy.spring.base.dto.response.toResponseEntity
import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto
import com.vjcspy.spring.packages.stockinfo.service.CorEntityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cors")
class CorController(
    private val corEntityService: CorEntityService,
) {
    /**
     * Lấy danh sách tất cả CorEntity
     *
     * @return ResponseEntity chứa List<CorEntityDto>
     */
    @GetMapping
    fun getAllCorEntities(): ResponseEntity<OkResponse<List<CorEntityDto>>> {
        val corEntityDtos = corEntityService.getAllCorEntities()

        return OkResponse.of(corEntityDtos).toResponseEntity()
    }

    /**
     * Sync all Cors
     *
     * @return ResponseEntity
     */
    @PostMapping("/sync")
    fun syncCorEntity(): ResponseEntity<OkResponse<Nothing?>> = OkResponse.of(null).toResponseEntity()
}
