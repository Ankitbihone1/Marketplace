package com.bajrix.marketplace.service;

import com.bajrix.marketplace.dto.CreateListingRequest;
import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.dto.UpdateListingRequest;
import com.bajrix.marketplace.entity.Product;
import com.bajrix.marketplace.entity.Seller;
import com.bajrix.marketplace.entity.SellerListing;
import com.bajrix.marketplace.exception.ResourceNotFoundException;
import com.bajrix.marketplace.repository.ProductRepository;
import com.bajrix.marketplace.repository.SellerListingRepository;
import com.bajrix.marketplace.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerListingRepository sellerListingRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller seller;
    private Product product;
    private SellerListing listing;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Test Seller");

        product = new Product();
        product.setId(10L);
        product.setName("Test Product");

        listing = SellerListing.builder()
                .id(100L)
                .seller(seller)
                .product(product)
                .price(new BigDecimal("99.99"))
                .stock(10)
                .minOrderQuantity(1)
                .isActive(true)
                .version(1L)
                .build();
    }

    @Test
    void addListing_Success() {
        CreateListingRequest request = new CreateListingRequest();
        request.setProductId(10L);
        request.setPrice(new BigDecimal("99.99"));
        request.setStock(10);
        request.setMinOrderQuantity(1);
        request.setIsActive(true);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(sellerListingRepository.save(any(SellerListing.class))).thenReturn(listing);

        SellerListingResponse response = sellerService.addListing(1L, request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Test Product", response.getProductName());
        verify(sellerListingRepository, times(1)).save(any(SellerListing.class));
    }

    @Test
    void addListing_SellerNotFound_ThrowsException() {
        CreateListingRequest request = new CreateListingRequest();
        request.setProductId(10L);

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.addListing(1L, request));
        verify(sellerListingRepository, never()).save(any(SellerListing.class));
    }
    
    @Test
    void addListing_InvalidPrice_ThrowsException() {
        CreateListingRequest request = new CreateListingRequest();
        request.setProductId(10L);
        request.setPrice(new BigDecimal("-10.00")); // Invalid price

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> sellerService.addListing(1L, request));
        verify(sellerListingRepository, never()).save(any(SellerListing.class));
    }

    @Test
    void updateListing_Success() {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setPrice(new BigDecimal("89.99"));
        request.setStock(20);

        when(sellerListingRepository.findByIdAndSellerId(100L, 1L)).thenReturn(Optional.of(listing));
        when(sellerListingRepository.save(any(SellerListing.class))).thenReturn(listing);

        SellerListingResponse response = sellerService.updateListing(1L, 100L, request);

        assertNotNull(response);
        assertEquals(new BigDecimal("89.99"), listing.getPrice());
        assertEquals(20, listing.getStock());
        verify(sellerListingRepository, times(1)).save(listing);
    }

    @Test
    void updateListing_ListingNotFound_ThrowsException() {
        UpdateListingRequest request = new UpdateListingRequest();

        when(sellerListingRepository.findByIdAndSellerId(100L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.updateListing(1L, 100L, request));
        verify(sellerListingRepository, never()).save(any(SellerListing.class));
    }

    @Test
    void deleteListing_Success() {
        when(sellerListingRepository.findByIdAndSellerId(100L, 1L)).thenReturn(Optional.of(listing));

        sellerService.deleteListing(1L, 100L);

        verify(sellerListingRepository, times(1)).delete(listing);
    }

    @Test
    void deleteListing_ListingNotFound_ThrowsException() {
        when(sellerListingRepository.findByIdAndSellerId(100L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.deleteListing(1L, 100L));
        verify(sellerListingRepository, never()).delete(any(SellerListing.class));
    }
}
