package com.bajrix.marketplace.controller;

import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_Success() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductListings_Success() throws Exception {
        SellerListingResponse response = SellerListingResponse.builder()
                .id(1L)
                .productId(10L)
                .productName("Sample Product")
                .build();
                
        when(productService.findProductListings(anyLong())).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/products/10/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(10))
                .andExpect(jsonPath("$[0].productName").value("Sample Product"));
    }
}
