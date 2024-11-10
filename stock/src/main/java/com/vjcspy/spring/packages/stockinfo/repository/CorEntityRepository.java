/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.repository;

import com.vjcspy.spring.packages.stockinfo.entity.CorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorEntityRepository extends JpaRepository<CorEntity, Integer> {
    // Các phương thức custom query có thể được khai báo ở đây nếu cần thiết
    // Ví dụ: Tìm kiếm bằng code
    Optional<CorEntity> findByCode(String code);
}
