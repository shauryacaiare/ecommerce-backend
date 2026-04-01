package com.ecommerce.backend.service.utility;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.dto.CompanyDto;
import com.ecommerce.backend.dto.requestdto.CreateProductReqDto;
import com.ecommerce.backend.dto.requestdto.CreateSellerRequestDto;
import com.ecommerce.backend.dto.responsedto.CreateCategoryResDto;
import com.ecommerce.backend.dto.responsedto.OrderItemResponseDto;
import com.ecommerce.backend.dto.responsedto.OrderResponseDto;
import com.ecommerce.backend.dto.responsedto.ProductsDetailsDto;
import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.CompanyRepo;
import com.ecommerce.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utility {

    private final CompanyRepo companyRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public Seller createSeller(CreateSellerRequestDto requestDto) {
        Long companyId = requestDto.getCompanyId();
        String sellerEmail = requestDto.getEmail();
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company","companyId",String.valueOf(companyId)));
        if(userRepo.existsByEmail(sellerEmail)){
            throw new ResourceAlreadyExistException(sellerEmail);
        }
        return Seller.builder()
                .company(company)
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .userType(UserType.SELLER)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .gstNumber(requestDto.getGstNumber())
                .registrationId(requestDto.getRegistrationId())
                .build();

    }

    public CreateCategoryResDto mapToCreateCategoryResDto(Category category) {
        return CreateCategoryResDto.builder()
                .id(category.getId())
                .name(category.getName())
                .desc(category.getDescription())
                .build();
    }

    public Product getProduct(CreateProductReqDto productReqDto, Company company, Set<Category> categories) {
        return Product.builder()
                .name(productReqDto.getName())
                .desc(productReqDto.getDesc())
                .stock(productReqDto.getStock())
                .isActive(productReqDto.getIsActive())
                .categories(categories)
                .company(company)
                .price(productReqDto.getPrice())
                .build();
    }
    public String getLoggedInUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public ProductsDetailsDto getProductsDetailsDto(Product product) {
        return ProductsDetailsDto.builder()
                .productId(product.getId())
                .stock(product.getStock())
                .price(product.getPrice())
                .desc(product.getDesc())
                .isActive(product.getIsActive())
                .name(product.getName())
                .companyDto(getCompanyDto(product))
                .categoryDtos(product.getCategories().stream()
                        .map(category -> CategoryDto.builder()
                                .id(category.getId())
                                .desc(category.getDescription())
                                .name(category.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private CompanyDto getCompanyDto(Product product) {
        Company company = product.getCompany();
        return CompanyDto.builder()
                .companyId(company.getId())
                .email(company.getEmail())
                .name(company.getName())
                .number(company.getNumber())
                .isActive(company.getIsActive())
                .email(company.getEmail())
                .build();
    }

    private List<OrderItemResponseDto> mapToOrderItemResponseDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderItemResponseDto.builder()
                        .orderItemId(orderItem.getId())
                        .productName(orderItem.getProduct().getName())
                        .productId(orderItem.getProduct().getId())
                        .price(orderItem.getPrice())
                        .itemTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList());
    }

    public OrderResponseDto mapToOrderResponseDto(Order savedOrder) {

        return OrderResponseDto.builder()
                .orderItems(mapToOrderItemResponseDto(savedOrder.getOrderItems()))
                .orderId(savedOrder.getId())
                .totalPrice(savedOrder.getTotalPrice())
                .orderStatus(savedOrder.getOrderStatus())
                .createdAt(savedOrder.getCreatedAt())
                .updatedAt(savedOrder.getUpdatedAt())
                .build();
    }
}
