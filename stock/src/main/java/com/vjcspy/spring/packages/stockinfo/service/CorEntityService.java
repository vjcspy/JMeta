/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.service;

import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto;
import com.vjcspy.spring.packages.stockinfo.entity.CorEntity;
import com.vjcspy.spring.packages.stockinfo.mapper.CorEntityMapper;
import com.vjcspy.spring.packages.stockinfo.repository.CorEntityRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorEntityService {

    private final CorEntityRepository corEntityRepository;

    @Autowired
    public CorEntityService(CorEntityRepository corEntityRepository) {
        this.corEntityRepository = corEntityRepository;
    }

    /**
     * Lấy danh sách tất cả CorEntity và chuyển sang CorEntityDto
     *
     * @return List<CorEntityDto>
     */
    public List<CorEntityDto> getAllCorEntities() {
        List<CorEntity> entities = corEntityRepository.findAll();
        return entities.stream().map(CorEntityMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
