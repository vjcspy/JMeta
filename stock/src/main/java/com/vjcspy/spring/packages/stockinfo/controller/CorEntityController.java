/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.controller;

import com.vjcspy.spring.base.dto.response.OkResponse;
import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto;
import com.vjcspy.spring.packages.stockinfo.service.CorEntityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cors")
public class CorEntityController {

    @Autowired
    private CorEntityService corEntityService;

    /**
     * API lấy danh sách tất cả CorEntity
     *
     * @return ResponseEntity chứa List<CorEntityDto>
     */
    @GetMapping
    public ResponseEntity<OkResponse<?>> getAllCorEntities() {
        List<CorEntityDto> corEntityDtos = corEntityService.getAllCorEntities();
        return ResponseEntity.ok(OkResponse.of(corEntityDtos));
    }
}
