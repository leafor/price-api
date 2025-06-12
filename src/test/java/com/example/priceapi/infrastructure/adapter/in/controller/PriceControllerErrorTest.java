package com.example.priceapi.infrastructure.adapter.in.controller;

import com.example.priceapi.application.port.in.FindPriceUseCase;
import com.example.priceapi.infrastructure.adapter.in.exception.RestExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {
        PriceController.class,
        RestExceptionHandler.class
})
class PriceControllerErrorTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    FindPriceUseCase findPriceUseCase;

    @Nested
    @DisplayName("❌ Bad Request 400")
    class BadRequestTests {

        @Test
        @DisplayName("Missing applicationDate → 400")
        void missingDate_thenBadRequest() throws Exception {
            mvc.perform(get("/api/prices")
                            .param("productId","35455")
                            .param("brandId","1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("applicationDate")))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
        }

        @Test
        @DisplayName("Invalid date format → 400")
        void badDateFormat_thenBadRequest() throws Exception {
            mvc.perform(get("/api/prices")
                            .param("applicationDate","not-a-date")
                            .param("productId","35455")
                            .param("brandId","1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("applicationDate")))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
        }
    }

    @Nested
    @DisplayName("❌ Method Not Allowed 405")
    class MethodNotAllowedTest {
        @Test
        @DisplayName("POST on GET endpoint → 405")
        void postOnGet_thenMethodNotAllowed() throws Exception {
            mvc.perform(post("/api/prices")
                            .param("applicationDate","2020-06-14T16:00:00Z")
                            .param("productId","35455")
                            .param("brandId","1"))
                    .andExpect(status().isMethodNotAllowed());
        }

        @Test
        @DisplayName("POST on GET endpoint → 405")
        void getBadUrl_thenMethodNotAllowed() throws Exception {
            mvc.perform(get("/api/pricXes")
                            .param("applicationDate","2020-06-14T16:00:00Z")
                            .param("productId","35455")
                            .param("brandId","1"))
                    .andExpect(status().isMethodNotAllowed());
        }
    }

    @Nested
    @DisplayName("❌ Internal Server Error 500")
    class InternalErrorTest {

        @Test
        @DisplayName("Service throws → 500")
        void serviceThrows_thenInternalServerError() throws Exception {
            doThrow(new RuntimeException("boom"))
                    .when(findPriceUseCase)
                    .findPrice(any(),any(),any());

            mvc.perform(get("/api/prices")
                            .param("applicationDate","2020-06-14T16:00:00Z")
                            .param("productId","35455")
                            .param("brandId","1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                    .andExpect(jsonPath("$.code").value(500))
                    .andExpect(jsonPath("$.message",
                            containsString("Internal Server Error")));
        }
    }
}