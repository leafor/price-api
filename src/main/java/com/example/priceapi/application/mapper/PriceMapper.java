package com.example.priceapi.application.mapper;

import com.example.priceapi.domain.port.out.PriceValues;
import com.example.priceapi.application.dto.PriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "brandId",   source = "brandId")
    PriceResponse toResponse(PriceValues values, Long productId, Integer brandId);
}
