/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.controller;

import com.vjcspy.spring.base.dto.response.OkResponse;
import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto;
import com.vjcspy.spring.packages.stockinfo.service.CorEntityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cors")
public class CorEntityController {

    @Autowired
    private CorEntityService corEntityService;

    /**
     * Lấy danh sách tất cả CorEntity
     *
     * @return ResponseEntity chứa List<CorEntityDto>
     */
    @GetMapping
    public ResponseEntity<OkResponse<?>> getAllCorEntities() {
        List<CorEntityDto> corEntityDtos = corEntityService.getAllCorEntities();
        return ResponseEntity.ok(OkResponse.of(corEntityDtos));
    }

    /**
     * Sync all Cors
     *
     * @return ResponseEntity
     */
    @PostMapping("/sync")
    public ResponseEntity<OkResponse<?>> syncCorEntity() {
        return ResponseEntity.ok(OkResponse.of(null));
    }
}
