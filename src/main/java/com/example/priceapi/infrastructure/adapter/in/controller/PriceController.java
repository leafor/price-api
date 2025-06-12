package com.example.priceapi.infrastructure.adapter.in.controller;

import com.example.priceapi.application.dto.PriceResponse;
import com.example.priceapi.application.port.in.FindPriceUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    private final FindPriceUseCase findPriceUseCase;

    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    public PriceController(FindPriceUseCase findPriceUseCase) {
        this.findPriceUseCase = findPriceUseCase;
    }
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(@RequestParam("applicationDate")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  OffsetDateTime applicationDate,
                                                  @RequestParam("productId")
                                                      Long productId,
                                                  @RequestParam("brandId")
                                                      Integer brandId) {
        log.info("GET /api/prices - applicationDate={}, productId={}, brandId={}",
                applicationDate, productId, brandId);
        return ResponseEntity.ok(findPriceUseCase.findPrice(productId,brandId,applicationDate));
    }
}
