package com.bajrix.marketplace.repository;

import com.bajrix.marketplace.entity.SellerListing;
import com.bajrix.marketplace.entity.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerListingRepository extends JpaRepository<SellerListing, Long> {
    List<SellerListing> findByProductIdAndSellerStatus(Long productId, SellerStatus status);
    List<SellerListing> findBySellerId(Long sellerId);
    Optional<SellerListing> findByIdAndSellerId(Long id, Long sellerId);
}
