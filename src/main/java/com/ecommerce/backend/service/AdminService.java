package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.requestdto.CompanyReqDto;
import com.ecommerce.backend.dto.requestdto.CreateSellerRequestDto;
import com.ecommerce.backend.dto.responsedto.CompanyResponseDto;
import com.ecommerce.backend.dto.responsedto.CreateSellerResponseDto;
import com.ecommerce.backend.entity.Company;
import com.ecommerce.backend.entity.Seller;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.CompanyRepo;
import com.ecommerce.backend.repository.SellerRepo;
import com.ecommerce.backend.service.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final SellerRepo sellerRepo;
    private final CompanyRepo companyRepo;
    private final Utility utility;

    @Transactional
    public CreateSellerResponseDto saveSeller(CreateSellerRequestDto requestDto){
        Seller seller =utility.createSeller(requestDto);
        Seller savedSeller = sellerRepo.save(seller);
        return CreateSellerResponseDto.builder()
                .name(savedSeller.getName())
                .companyId(savedSeller.getCompany().getId())
                .email(savedSeller.getEmail())
                .gstNumber(savedSeller.getGstNumber())
                .registrationId(savedSeller.getRegistrationId())
                .build();
    }



    @Transactional
    public CompanyResponseDto createCompany(CompanyReqDto reqDto) {
       if(companyRepo.existsByEmail(reqDto.getEmail())){
           throw new ResourceAlreadyExistException("Company email", reqDto.getEmail());
       }

       Company company = Company.builder()
               .name(reqDto.getName())
               .number(reqDto.getNumber())
               .email(reqDto.getEmail())
               .isActive(reqDto.getIsActive())
               .build();
       Company savedCompany = companyRepo.save(company);
       return CompanyResponseDto.builder()
               .companyId(savedCompany.getId())
               .name(savedCompany.getName())
               .email(savedCompany.getEmail())
               .number(savedCompany.getNumber())
               .isActive(savedCompany.getIsActive())
               .build();
    }

    @Transactional(readOnly = true)
    public List<CreateSellerResponseDto> getAllSellers() {
        List<Seller> sellers = sellerRepo.findAll();

        return sellers.stream()
                .map(seller -> CreateSellerResponseDto.builder()
                        .name(seller.getName())
                        .registrationId(seller.getRegistrationId())
                        .companyId(seller.getCompany().getId())
                        .email(seller.getEmail())
                        .gstNumber(seller.getGstNumber())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateSellerResponseDto deleteSeller(Long sellerId) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller","seller with Id",sellerId));

        CreateSellerResponseDto response = mapToDto(seller);

        sellerRepo.delete(seller);

        return response;
    }

    private CreateSellerResponseDto mapToDto(Seller seller) {
        return CreateSellerResponseDto.builder()
                .name(seller.getName())
                .registrationId(seller.getRegistrationId())
                .companyId(seller.getCompany().getId())
                .gstNumber(seller.getGstNumber())
                .email(seller.getEmail())
                .build();

    }
}
