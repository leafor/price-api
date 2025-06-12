package com.example.priceapi.infrastructure.adapter.in.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorMessage {
    private String status;
    private Integer code;
    private String message;
    private String timestamp;
    private String resource;
}