package com.example.priceapi.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = NONE)
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("✅ Happy path y 404")
    class HappyAndNotFound {

        @Test
        @DisplayName("When valid params then all fields returned")
        void whenValidParams_thenAllFieldsReturned() throws Exception {
            mvc.perform(get("/api/prices")
                            .param("applicationDate", "2020-06-14T16:00:00Z")
                            .param("productId", "35455")
                            .param("brandId", "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(2))
                    .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00Z"))
                    .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00Z"))
                    .andExpect(jsonPath("$.price").value(25.45));
        }

        @Test
        @DisplayName("When no price found then 404")
        void whenNoPrice_thenReturnsNotFound() throws Exception {
            mvc.perform(get("/api/prices")
                            .param("applicationDate", "2021-12-31T10:00:00Z")
                            .param("productId", "35455")
                            .param("brandId", "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message", containsString("No existe tarifa")))
                    .andExpect(jsonPath("$.message", containsString("productId=35455")))
                    .andExpect(jsonPath("$.message", containsString("brandId=1")))
                    .andExpect(jsonPath("$.message", containsString("2021-12-31T10:00:00Z")));
        }
    }
}