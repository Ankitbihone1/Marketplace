package com.bajrix.marketplace.dto;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;

@Data
@Builder
public class SellerListingResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Long sellerId;
    private String sellerName;
    private BigDecimal price;
    private Integer stock;
    private Integer minOrderQuantity;
    private Boolean isActive;
    private Long version;
}
