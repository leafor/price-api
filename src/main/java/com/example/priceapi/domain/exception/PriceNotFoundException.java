package com.example.priceapi.domain.exception;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Business exception indicating that no price was found for the specified
 * product, brand, and application date.
 * <p>
 * Typically thrown by the {@link com.example.priceapi.domain.service.PriceFinder}
 * when the repository returns empty.
 * </p>
 */
public class PriceNotFoundException extends RuntimeException {

    private static final DateTimeFormatter ISO_WITH_SECONDS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * Constructs a new PriceNotFoundException with a detailed message that
     * includes the product ID, brand ID, and application date formatted in
     * ISO-8601 with seconds and offset.
     *
     * @param productId       the identifier of the product for which no price was found
     * @param brandId         the identifier of the brand (chain) for which no price was found
     * @param applicationDate the date and time at which the price was requested
     */
    public PriceNotFoundException(Long productId, Integer brandId, OffsetDateTime applicationDate) {
        super(String.format(
                "No existe tarifa para productId=%d, brandId=%d en fecha=%s",
                productId, brandId, applicationDate.format(ISO_WITH_SECONDS)
        ));
    }
}