package com.example.priceapi.infrastructure.adapter.in.config;

import com.example.priceapi.domain.port.out.PriceRepositoryPort;
import com.example.priceapi.domain.service.PriceFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceFinderConfig {

    @Bean
    public PriceFinder priceFinder(PriceRepositoryPort repo) {
        return new PriceFinder(repo);
    }
}