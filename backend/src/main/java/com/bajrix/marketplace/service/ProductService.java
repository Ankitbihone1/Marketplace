package com.bajrix.marketplace.service;

import com.bajrix.marketplace.dto.ProductResponse;
import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.entity.Product;
import com.bajrix.marketplace.entity.SellerStatus;
import com.bajrix.marketplace.exception.ResourceNotFoundException;
import com.bajrix.marketplace.repository.ProductRepository;
import com.bajrix.marketplace.repository.SellerListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerListingRepository sellerListingRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(p -> ProductResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .category(p.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SellerListingResponse> findProductListings(Integer productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return sellerListingRepository
                .findByProductIdAndSellerStatus(
                        productId,
                        SellerStatus.APPROVED
                )
                .stream()
                .map(listing -> SellerListingResponse.builder()
                        .id(listing.getId())
                        .productId(listing.getProduct().getId())
                        .productName(listing.getProduct().getName())
                        .sellerId(listing.getSeller().getId())
                        .sellerName(listing.getSeller().getName())
                        .price(listing.getPrice())
                        .quantity(listing.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}