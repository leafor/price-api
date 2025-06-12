package com.example.priceapi.application.port.in;

import com.example.priceapi.application.dto.PriceResponse;
import java.time.OffsetDateTime;

public interface FindPriceUseCase {
    PriceResponse findPrice(Long productId, Integer brandId, OffsetDateTime date);
}