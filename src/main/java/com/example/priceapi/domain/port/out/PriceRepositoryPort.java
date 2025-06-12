package com.example.priceapi.domain.port.out;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {
    Optional<PriceValues> findApplicablePrice(Long productId, Integer brandId, OffsetDateTime applicationDate);
}
