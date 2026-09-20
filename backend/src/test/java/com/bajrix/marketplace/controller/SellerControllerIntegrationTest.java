package com.bajrix.marketplace.controller;

import com.bajrix.marketplace.dto.CreateListingRequest;
import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.dto.UpdateListingRequest;
import com.bajrix.marketplace.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SellerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMyListings_Success() throws Exception {
        when(sellerService.findSellerListings(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sellers/listings")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getMyListings_MissingHeader_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/sellers/listings"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getMyListings_InvalidHeader_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/sellers/listings")
                        .header("X-User-Id", "not-a-number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createListing_Success() throws Exception {
        CreateListingRequest request = new CreateListingRequest();
        request.setProductId(10L);
        request.setPrice(new BigDecimal("99.99"));
        request.setStock(10);
        
        SellerListingResponse response = SellerListingResponse.builder()
                .id(100L)
                .productId(10L)
                .sellerId(1L)
                .price(new BigDecimal("99.99"))
                .build();

        when(sellerService.addListing(eq(1L), any(CreateListingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/sellers/listings")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.productId").value(10));
    }

    @Test
    void createListing_MissingHeader_ReturnsBadRequest() throws Exception {
        CreateListingRequest request = new CreateListingRequest();
        request.setProductId(10L);

        mockMvc.perform(post("/api/sellers/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateListing_Success() throws Exception {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setPrice(new BigDecimal("89.99"));
        
        SellerListingResponse response = SellerListingResponse.builder()
                .id(100L)
                .price(new BigDecimal("89.99"))
                .build();

        when(sellerService.updateListing(eq(1L), eq(100L), any(UpdateListingRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/sellers/listings/100")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(89.99));
    }

    @Test
    void deleteListing_Success() throws Exception {
        mockMvc.perform(delete("/api/sellers/listings/100")
                        .header("X-User-Id", 1L))
                .andExpect(status().isNoContent());

        verify(sellerService).deleteListing(1L, 100L);
    }
}
