package com.bajrix.marketplace.controller;

import com.bajrix.marketplace.dto.ProductResponse;
import com.bajrix.marketplace.dto.SellerListingResponse;
import com.bajrix.marketplace.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}/listings")
    public ResponseEntity<List<SellerListingResponse>> getProductListings(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findProductListings(id));
    }
}
