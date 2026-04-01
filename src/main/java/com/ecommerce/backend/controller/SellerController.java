package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductResponseDto;
import com.ecommerce.backend.dto.requestdto.CreateProductReqDto;
import com.ecommerce.backend.dto.requestdto.UpdateProductReqDto;
import com.ecommerce.backend.dto.responsedto.ApiResponseDto;
import com.ecommerce.backend.dto.responsedto.CreateProductResponseDto;
import com.ecommerce.backend.dto.responsedto.ProductsDetailsDto;
import com.ecommerce.backend.dto.responsedto.UpdateProductResDto;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/seller")
@Slf4j
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<CreateProductResponseDto> createProduct(@Valid @RequestBody CreateProductReqDto productReqDto){
        log.info("request received to create product : {}",productReqDto);
        CreateProductResponseDto responseDto = sellerService.createProduct(productReqDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<ProductsDetailsDto> getProductById(
            @PathVariable Long productId) {
        log.info("get product by id: {}", productId);
        return ResponseEntity.ok(sellerService.getProductById(productId));
    }

    @GetMapping("/products")
    public ResponseEntity<Set<ProductsDetailsDto>> getAllProductsBySeller(){
        Set<ProductsDetailsDto> allProductsBySeller = sellerService.getAllProductsBySeller();
        return ResponseEntity.ok(allProductsBySeller);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<UpdateProductResDto> updateProduct(@Valid @RequestBody UpdateProductReqDto updateProductReqDto
            ,@PathVariable Long productId){
        log.info("update request request received : {}",updateProductReqDto);
        UpdateProductResDto updateProductResDto = sellerService.updateProduct(updateProductReqDto,productId);
        return ResponseEntity.ok(updateProductResDto);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ApiResponseDto> deleteSeller(@PathVariable Long productId){
        sellerService.deleteProduct(productId);
        return new ResponseEntity<>(
                ApiResponseDto.builder()
                        .message("Product deleted successfully")
                        .success(true)
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.OK);
    }
}
