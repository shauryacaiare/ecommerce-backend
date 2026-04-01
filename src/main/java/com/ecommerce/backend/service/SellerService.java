package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.requestdto.CreateProductReqDto;
import com.ecommerce.backend.dto.requestdto.UpdateProductReqDto;
import com.ecommerce.backend.dto.responsedto.CreateProductResponseDto;
import com.ecommerce.backend.dto.responsedto.ProductsDetailsDto;
import com.ecommerce.backend.dto.responsedto.UpdateProductResDto;
import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.CategoryRepo;
import com.ecommerce.backend.repository.ProductRepo;
import com.ecommerce.backend.repository.SellerRepo;
import com.ecommerce.backend.service.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerRepo sellerRepo;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final Utility utility;


    @Transactional
    public CreateProductResponseDto createProduct(CreateProductReqDto productReqDto) {
        String name = productReqDto.getName();
        String sellerEmail= utility.getLoggedInUserEmail();
        Seller seller = sellerRepo.findByEmailWithCompany(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "email", sellerEmail));
        Company company = seller.getCompany();
        Long companyId = company.getId();
        if(productRepo.existsByNameAndCompanyId(name,companyId)){
            throw new ResourceAlreadyExistException("company with name",name);
        }
        Set<Long> requestedIds = productReqDto.getCategories();
        Set<Category> categories = categoryRepo.findAllByIdIn(requestedIds);
        if(categories.size() != requestedIds.size()) {
            throw new ResourceNotFoundException("Category", "ids", requestedIds.toString());
        }
        Product product = utility.getProduct(productReqDto, company, categories);

        Product savedProduct = productRepo.save(product);
        return getCreateProductResponseDto(productReqDto, savedProduct);
    }

    private static CreateProductResponseDto getCreateProductResponseDto(CreateProductReqDto productReqDto, Product savedProduct) {
        return CreateProductResponseDto.builder()
                .name(savedProduct.getName())
                .desc(savedProduct.getDesc())
                .price(savedProduct.getPrice())
                .companyId(savedProduct.getCompany().getId())
                .stock(productReqDto.getStock())
                .categories(savedProduct.
                        getCategories().
                        stream().map(Category::getId).collect(Collectors.toSet()))
                .productId(savedProduct.getId())
                .isActive(savedProduct.getIsActive())
                .build();
    }

    @Transactional(readOnly = true)
    public Set<ProductsDetailsDto> getAllProductsBySeller() {
        String sellerEmail = utility.getLoggedInUserEmail();
        Seller seller = sellerRepo.findByEmailWithAllDetails(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller","Seller with Id",sellerEmail));
        return seller.getCompany().getProducts().stream()
                .map(utility::getProductsDetailsDto)
                .collect(Collectors.toSet());
    }



    @Transactional
    public UpdateProductResDto updateProduct(UpdateProductReqDto updateProductReqDto, Long productId) {
        String sellerEmail = utility.getLoggedInUserEmail();
        Seller seller = sellerRepo.findByEmailWithCompany(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller","Email",sellerEmail));
        Product product = productRepo.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","product with Id",productId));
        if(!seller.getCompany().getId().equals(product.getCompany().getId())){
            throw new AccessDeniedException("product does not belong the seller");
        }
        if (updateProductReqDto.getName() != null)        product.setName(updateProductReqDto.getName());
        if (updateProductReqDto.getDesc() != null)        product.setDesc(updateProductReqDto.getDesc());
        if (updateProductReqDto.getPrice() != null)       product.setPrice(updateProductReqDto.getPrice());
        if (updateProductReqDto.getStock() != null)       product.setStock(updateProductReqDto.getStock());
        if (updateProductReqDto.getIsActive() != null)    product.setIsActive(updateProductReqDto.getIsActive());

        if (updateProductReqDto.getCategories() != null && !updateProductReqDto.getCategories().isEmpty()) {
            Set<Category> categories = categoryRepo.findAllByIdIn(updateProductReqDto.getCategories());
            if (categories.size() != updateProductReqDto.getCategories().size()) {
                throw new ResourceNotFoundException("Category", "ids",
                        updateProductReqDto.getCategories().toString());
            }
            product.setCategories(categories);
        }

        // 4. Save — Hibernate auto updates product_category join table too
        Product updated = productRepo.save  (product);

        return UpdateProductResDto.builder()
                .id(updated.getId())
                .name(updated.getName())
                .desc(updated.getDesc())
                .price(updated.getPrice())
                .stock(updated.getStock())
                .isActive(updated.getIsActive())
                .companyId(updated.getCompany().getId())
                .categories(updated.getCategories()
                        .stream().map(Category::getId).collect(Collectors.toSet()))
                .updatedAt(updated.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        String sellerEmail = utility.getLoggedInUserEmail();
        Seller seller = sellerRepo.findByEmailWithCompany(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("seller","seller Email",sellerEmail));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product","productId",productId));
        if(!product.getCompany().getId().equals(seller.getCompany().getId())){
            throw new AccessDeniedException("seller does not have permission to access the product with id : "+productId);
        }
        productRepo.delete(product);
    }


    @Transactional(readOnly = true)
    public ProductsDetailsDto getProductById(Long productId) {
        String sellerEmail = utility.getLoggedInUserEmail();
        Seller seller = sellerRepo.findByEmailWithCompany(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("seller","seller Email",sellerEmail));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product","productId",productId));
        if(!product.getCompany().getId().equals(seller.getCompany().getId())){
            throw new AccessDeniedException("seller does not have permission to access the product with id : "+productId);
        }
        return utility.getProductsDetailsDto(product);
    }
}
