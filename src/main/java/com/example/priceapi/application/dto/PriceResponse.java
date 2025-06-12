package com.example.priceapi.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PriceResponse(
    long productId,
    int brandId,
    int priceList,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    BigDecimal price
) {}
