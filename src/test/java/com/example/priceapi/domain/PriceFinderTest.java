package com.example.priceapi.domain;

import com.example.priceapi.domain.exception.PriceNotFoundException;
import com.example.priceapi.domain.port.out.PriceRepositoryPort;
import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.domain.service.PriceFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceFinderTest {
    @Mock
    PriceRepositoryPort repo;

    @InjectMocks
    PriceFinder finder;

    @Test
    @DisplayName("Devuelve PriceValues cuando existe tarifa")
    void whenExists_thenReturnValues() {
        OffsetDateTime now = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        PriceValues vals = new PriceValues(1, now.minusSeconds(3600), now.plusSeconds(3600), BigDecimal.valueOf(35.5));
        when(repo.findApplicablePrice(35455L, 1, now)).thenReturn(Optional.of(vals));

        PriceValues result = finder.findPrice(35455L, 1, now);

        assertThat(result).isEqualTo(vals);
    }

    @Test
    @DisplayName("Lanza PriceNotFoundException con mensaje FORMATO completo")
    void whenNotExists_thenThrowWithFullTimestamp() {
        OffsetDateTime now = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        when(repo.findApplicablePrice(anyLong(), anyInt(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> finder.findPrice(35455L, 1, now))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessage(String.format(
                        "No existe tarifa para productId=%d, brandId=%d en fecha=%s",
                        35455L, 1, "2020-06-14T10:00:00Z"
                ));
    }

    @ParameterizedTest(name = "[{index}] productId={0}, brandId={1}, date={2}")
    @CsvSource({
            "35455, 1, 2020-06-14T10:00:00Z",
            "12345, 2, 2021-01-01T00:00:00Z"
    })
    @DisplayName("Parametrizado: invoca repo con distintos parámetros")
    void parameterized_invokesRepo(Long productId, Integer brandId, String dateStr) {
        OffsetDateTime date = OffsetDateTime.parse(dateStr);
        PriceValues vals = new PriceValues(1, date, date, BigDecimal.ONE);
        when(repo.findApplicablePrice(productId, brandId, date)).thenReturn(Optional.of(vals));

        finder.findPrice(productId, brandId, date);

        verify(repo).findApplicablePrice(productId, brandId, date);
    }
}