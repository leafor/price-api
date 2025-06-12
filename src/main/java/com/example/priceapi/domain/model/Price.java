package com.example.priceapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class Price {
    private final Long id;
    private final Integer brandId;
    private final Long productId;
    private final Integer priceList;
    private final Integer priority;
    private final BigDecimal price;
    private final String currency;
    private final OffsetDateTime startDate;
    private final OffsetDateTime endDate;

    public boolean isApplicable(OffsetDateTime applicationDate) {
        return !applicationDate.isBefore(startDate) && !applicationDate.isAfter(endDate);
    }
}
