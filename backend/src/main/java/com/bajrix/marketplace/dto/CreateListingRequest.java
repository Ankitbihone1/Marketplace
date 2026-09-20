package com.bajrix.marketplace.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateListingRequest {
    private Long productId;
    private BigDecimal price;
    private Integer stock;
    private Integer minOrderQuantity;
    private Boolean isActive;
}
