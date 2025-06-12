package com.example.priceapi.application.service;

import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.application.dto.PriceResponse;
import com.example.priceapi.application.mapper.PriceMapper;
import com.example.priceapi.domain.service.PriceFinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PriceQueryServiceTest {

    @Mock
    PriceFinder finder;
    @Mock PriceMapper mapper;
    @InjectMocks
    PriceQueryService service;

    @Test
    void whenValidRequest_thenReturnsResponse() {
        OffsetDateTime now = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        PriceValues vals = new PriceValues(2, now, now.plusSeconds(3600), BigDecimal.valueOf(25.45));
        PriceResponse resp = new PriceResponse(35455L, 1, 2, now, now.plusSeconds(3600), BigDecimal.valueOf(25.45));

        when(finder.findPrice(35455L,1,now)).thenReturn(vals);
        when(mapper.toResponse(vals, 35455L, 1)).thenReturn(resp);

        PriceResponse result = service.findPrice(35455L,1, now);

        assertThat(result).isEqualTo(resp);
        verify(finder).findPrice(35455L,1,now);
        verify(mapper).toResponse(vals, 35455L, 1);
    }
}
