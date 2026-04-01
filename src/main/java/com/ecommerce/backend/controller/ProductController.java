package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductResponseDto;
import com.ecommerce.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ProductController.java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("browse products, keyword: {}, page: {}, size: {}", keyword, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(productService.getProductsByKeyword(keyword,pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable Long productId) {
        log.info("get product by id: {}", productId);
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
