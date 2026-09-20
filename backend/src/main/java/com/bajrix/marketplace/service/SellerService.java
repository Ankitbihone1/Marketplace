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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerListingRepository sellerListingRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<SellerListingResponse> findSellerListings(Long sellerId) {
        return sellerListingRepository.findBySellerId(sellerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SellerListingResponse addListing(Long sellerId, CreateListingRequest request) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getPrice() == null || request.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        SellerListing listing = SellerListing.builder()
                .seller(seller)
                .product(product)
                .price(request.getPrice())
                .stock(request.getStock())
                .minOrderQuantity(request.getMinOrderQuantity())
                .isActive(request.getIsActive())
                .build();

        listing = sellerListingRepository.save(listing);
        return mapToResponse(listing);
    }

    @Transactional
    public SellerListingResponse updateListing(Long sellerId, Long listingId, UpdateListingRequest request) {
        SellerListing listing = sellerListingRepository.findByIdAndSellerId(listingId, sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found for this seller"));

        if (request.getPrice() != null && request.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (request.getPrice() != null) listing.setPrice(request.getPrice());
        if (request.getStock() != null) listing.setStock(request.getStock());
        if (request.getMinOrderQuantity() != null) listing.setMinOrderQuantity(request.getMinOrderQuantity());
        if (request.getIsActive() != null) listing.setIsActive(request.getIsActive());
        if (request.getVersion() != null) listing.setVersion(request.getVersion());

        listing = sellerListingRepository.save(listing);
        return mapToResponse(listing);
    }

    @Transactional
    public void deleteListing(Long sellerId, Long listingId) {
        SellerListing listing = sellerListingRepository.findByIdAndSellerId(listingId, sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found for this seller"));
        sellerListingRepository.delete(listing);
    }

    private SellerListingResponse mapToResponse(SellerListing listing) {
        return SellerListingResponse.builder()
                .id(listing.getId())
                .productId(listing.getProduct().getId())
                .productName(listing.getProduct().getName())
                .sellerId(listing.getSeller().getId())
                .sellerName(listing.getSeller().getName())
                .price(listing.getPrice())
                .stock(listing.getStock())
                .minOrderQuantity(listing.getMinOrderQuantity())
                .isActive(listing.getIsActive())
                .version(listing.getVersion())
                .build();
    }
}
