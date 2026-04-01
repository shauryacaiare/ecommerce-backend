package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.dto.ProductResponseDto;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    @Transactional
    public Page<ProductResponseDto> getProductsByKeyword(String keyword, Pageable pageable) {
        Page<Product> products = keyword == null || keyword.isBlank()
                ? productRepo.findByIsActiveTrue(pageable)      // no keyword → return all active
                : productRepo.searchActiveProductsByKeyword(keyword,pageable);  // keyword → search

       return products.map(this::mapToProductResponseDto);
    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .desc(product.getDesc())
                .price(product.getPrice())
                .stock(product.getStock())
                .isActive(product.getIsActive())
                .companyId(product.getCompany().getId())
                .companyName(product.getCompany().getName())
                .categories(product.getCategories()
                        .stream()
                        .map(cat -> CategoryDto.builder()
                                .id(cat.getId())
                                .name(cat.getName())
                                .desc(cat.getDescription())
                                .build())
                        .collect(Collectors.toSet()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Transactional
    public ProductResponseDto getProductById(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","with id",productId));

        return mapToProductResponseDto(product);

    }
}
