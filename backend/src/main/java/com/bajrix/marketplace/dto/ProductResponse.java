package com.bajrix.marketplace.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ProductResponse {
    private Integer  id;
    private String name;
    private String description;
    private String category;
}
