package com.vjcspy.spring.packages.stockinfo.service;

import com.vjcspy.spring.packages.stockinfo.dto.CorEntityDto;
import com.vjcspy.spring.packages.stockinfo.entity.CorEntity;
import com.vjcspy.spring.packages.stockinfo.mapper.CorEntityMapper;
import com.vjcspy.spring.packages.stockinfo.repository.CorEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorEntityService {

    @Autowired
    private CorEntityRepository corEntityRepository;

    @Autowired
    private CorEntityMapper corEntityMapper;

    /**
     * Lấy danh sách tất cả CorEntity và chuyển sang CorEntityDto
     *
     * @return List<CorEntityDto>
     */
    public List<CorEntityDto> getAllCorEntities() {
        List<CorEntity> entities = corEntityRepository.findAll();
        return entities.stream()
                .map(corEntityMapper::toDto)
                .collect(Collectors.toList());
    }
}
