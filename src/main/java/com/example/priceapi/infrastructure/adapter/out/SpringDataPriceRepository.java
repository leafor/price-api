package com.example.priceapi.infrastructure.adapter.out;

import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.infrastructure.adapter.out.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    Optional<PriceValues> findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            Long productId,
            Integer brandId,
            OffsetDateTime applicationDateStart,
            OffsetDateTime applicationDateEnd
    );
}