package com.example.priceapi.domain.port.out;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PriceValues(
        Integer priceList,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        BigDecimal price
) {}