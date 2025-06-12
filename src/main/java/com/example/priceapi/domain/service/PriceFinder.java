package com.example.priceapi.domain.service;

import com.example.priceapi.domain.exception.PriceNotFoundException;
import com.example.priceapi.domain.port.out.PriceRepositoryPort;
import com.example.priceapi.domain.port.out.PriceValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

/**
 * Domain service responsible for finding and returning the applicable
 * price for a given product and brand at a specific date and time.
 * <p>
 * Encapsulates the selection logic and delegates persistence to
 * {@link PriceRepositoryPort}.
 * </p>
 */
public class PriceFinder {

    private final PriceRepositoryPort repository;

    private static final Logger log = LoggerFactory.getLogger(PriceFinder.class);

    /**
     * Constructs a new PriceFinder with the given repository port.
     *
     * @param repository the repository port used to look up price data
     */
    public PriceFinder(PriceRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Finds the applicable price values for the specified product, brand,
     * and application date.
     *
     * @param productId       the identifier of the product
     * @param brandId         the identifier of the brand (chain)
     * @param applicationDate the date and time at which the price should apply
     * @return a {@link PriceValues} object containing the price details
     * @throws PriceNotFoundException if no price is found for the given parameters
     */
    public PriceValues findPrice(
            Long productId,
            Integer brandId,
            OffsetDateTime applicationDate
    ) {
        log.debug("Buscando precio aplicable para productId={}, brandId={}, date={}",
                productId, brandId, applicationDate);
        return repository
                .findApplicablePrice(productId, brandId, applicationDate)
                .orElseThrow(() -> {
                    log.warn("No se encontró precio para productId={}, brandId={}, date={}",
                            productId, brandId, applicationDate);
                    return new PriceNotFoundException(productId, brandId, applicationDate);
                });
    }
}