package com.example.priceapi.application.service;

import com.example.priceapi.application.port.in.FindPriceUseCase;
import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.application.dto.PriceResponse;
import com.example.priceapi.application.mapper.PriceMapper;
import com.example.priceapi.domain.service.PriceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * Application service that implements the “query price” use case.
 * <p>
 * Delegates to the domain {@link PriceFinder} to retrieve the applicable
 * price values and then uses {@link PriceMapper} to convert them into
 * a {@link PriceResponse} suitable for API clients.
 * </p>
 */
@Service
public class PriceQueryService implements FindPriceUseCase {
    private static final Logger log = LoggerFactory.getLogger(PriceQueryService.class);
    private final PriceFinder priceFinder;
    private final PriceMapper mapper;

    public PriceQueryService(PriceFinder priceFinder, PriceMapper mapper) {
        this.priceFinder = priceFinder;
        this.mapper = mapper;
    }

    /**
     * Executes the price lookup operation for a given product, brand and date.
     * <p>
     * Logs the request parameters, invokes the domain service to obtain
     * {@link PriceValues}, and maps the result into a {@link PriceResponse}.
     * </p>
     *
     * @param productId       the identifier of the product to query
     * @param brandId         the identifier of the brand (chain)
     * @param applicationDate the date and time at which the price should apply
     * @return a {@link PriceResponse} containing productId, brandId,
     *         priceList, startDate, endDate, price and currency
     * @throws com.example.priceapi.domain.exception.PriceNotFoundException
     *         if no price is found for the given parameters
     */
    @Override
    public PriceResponse findPrice(Long productId, Integer brandId, OffsetDateTime applicationDate) {
        log.debug("Consultando precio para productId={}, brandId={}, date={}",
                productId, brandId, applicationDate);
        PriceValues price = priceFinder.findPrice(
                productId,
                brandId,
                applicationDate
        );
        PriceResponse response = mapper.toResponse(price, productId, brandId);
        log.info("Precio encontrado: productId={}, brandId={}, priceList={}, startDate={}",
                productId, brandId, response.priceList(), response.startDate());
        return response;
    }
}
