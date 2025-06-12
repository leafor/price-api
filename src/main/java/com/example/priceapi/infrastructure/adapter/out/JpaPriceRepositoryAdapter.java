package com.example.priceapi.infrastructure.adapter.out;

import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.domain.port.out.PriceRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;


@Repository
public class JpaPriceRepositoryAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository jpaRepo;

    public JpaPriceRepositoryAdapter(SpringDataPriceRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<PriceValues> findApplicablePrice(
            Long productId,
            Integer brandId,
            OffsetDateTime applicationDate
    ) {
        return jpaRepo.findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                productId, brandId, applicationDate, applicationDate
        );
    }
}
