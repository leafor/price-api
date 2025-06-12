package com.example.priceapi.infrastructure.adapter.in.exception;

import com.example.priceapi.application.port.in.FindPriceUseCase;
import com.example.priceapi.infrastructure.adapter.in.controller.PriceController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;

@WebMvcTest(controllers = PriceController.class)
@Import(RestExceptionHandler.class)
@DisplayName("RestExceptionHandler Tests")
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindPriceUseCase findPriceUseCase;

    @Nested
    @DisplayName("400 Bad Request (validation/type errors)")
    class BadRequest {

        @Test
        void whenInvalidDateFormat_thenReturnsBadRequest() throws Exception {
            mockMvc.perform(get("/api/prices")
                            .param("applicationDate", "bad-format")
                            .param("productId", "35455")
                            .param("brandId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message", containsString("applicationDate")));
        }

        // ... tus tests existentes de missing params ...
    }

    @Nested
    @DisplayName("404 Not Found (NotFoundException)")
    class NotFound {

        @Test
        void whenServiceThrowsNotFound_thenReturns404() throws Exception {
            // Simula que el servicio lanza tu excepción de negocio
            doThrow(new NotFoundException("No existe tarifa"))
                    .when(findPriceUseCase)
                    .findPrice(
                            anyLong(),
                            anyInt(),
                            ArgumentMatchers.any()
                    );

            mockMvc.perform(get("/api/prices")
                            .param("applicationDate", "2020-06-14T10:00:00Z")
                            .param("productId", "35455")
                            .param("brandId", "1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value("No existe tarifa"));
        }
    }

    @Nested
    @DisplayName("405 Method Not Allowed")
    class MethodNotAllowed {

        @Test
        void postOnGet_thenMethodNotAllowed() throws Exception {
            mockMvc.perform(get("/api/prices").with(request -> { request.setMethod("POST"); return request; })
                            .param("applicationDate", "2020-06-14T10:00:00Z")
                            .param("productId", "35455")
                            .param("brandId", "1"))
                    .andExpect(status().isMethodNotAllowed());
        }
    }

    @Nested
    @DisplayName("500 Internal Server Error")
    class InternalServerError {

        @Test
        void serviceThrowsRuntimeException_thenInternalServerError() throws Exception {
            doThrow(new RuntimeException("boom"))
                    .when(findPriceUseCase)
                    .findPrice(anyLong(), anyInt(), any());

            mockMvc.perform(get("/api/prices")
                            .param("applicationDate", "2020-06-14T10:00:00Z")
                            .param("productId", "35455")
                            .param("brandId", "1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                    .andExpect(jsonPath("$.code").value(500));
        }
    }

}