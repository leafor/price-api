package com.example.priceapi.application.mapper;

import com.example.priceapi.application.dto.PriceResponse;
import com.example.priceapi.domain.port.out.PriceValues;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class PriceMapperTest {

    private final PriceMapper mapper = Mappers.getMapper(PriceMapper.class);

    @Test
    void mapsValuesAndIdsCorrectly() {
        OffsetDateTime start = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        OffsetDateTime end   = OffsetDateTime.parse("2020-06-14T12:00:00Z");
        PriceValues vals = new PriceValues(4, start, end, BigDecimal.valueOf(38.95));

        PriceResponse resp = mapper.toResponse(vals, 35455L, 1);

        assertThat(resp.productId()).isEqualTo(35455L);
        assertThat(resp.brandId()).isEqualTo(1);
        assertThat(resp.priceList()).isEqualTo(4);
        assertThat(resp.startDate()).isEqualTo(start);
        assertThat(resp.endDate()).isEqualTo(end);
        assertThat(resp.price()).isEqualByComparingTo("38.95");
    }
}