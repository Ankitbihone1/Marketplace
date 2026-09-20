package com.bajrix.marketplace.controller;

import com.bajrix.marketplace.dto.CreateListingRequest;
import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.dto.UpdateListingRequest;
import com.bajrix.marketplace.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/listings")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<SellerListingResponse>> getMyListings(@RequestHeader("X-User-Id") Long sellerId) {
        return ResponseEntity.ok(sellerService.findSellerListings(sellerId));
    }

    @PostMapping
    public ResponseEntity<SellerListingResponse> createListing(
            @RequestHeader("X-User-Id") Long sellerId,
            @RequestBody CreateListingRequest request) {
        return ResponseEntity.ok(sellerService.addListing(sellerId, request));
    }

    @PutMapping("/{listingId}")
    public ResponseEntity<SellerListingResponse> updateListing(
            @RequestHeader("X-User-Id") Long sellerId,
            @PathVariable Long listingId,
            @RequestBody UpdateListingRequest request) {
        return ResponseEntity.ok(sellerService.updateListing(sellerId, listingId, request));
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteListing(
            @RequestHeader("X-User-Id") Long sellerId,
            @PathVariable Long listingId) {
        sellerService.deleteListing(sellerId, listingId);
        return ResponseEntity.noContent().build();
    }
}
